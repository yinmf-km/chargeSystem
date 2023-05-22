package com.course.app.webadmin.upms.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.core.annotation.NoAuthInterface;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.object.ResponseResult;
import com.course.app.common.core.object.TokenData;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.core.util.RedisKeyUtil;
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import com.course.app.webadmin.constant.ComConstant.CacheTime;
import com.course.app.webadmin.upms.dto.SysStudentDto;
import com.course.app.webadmin.upms.model.ProcessDetail;
import com.course.app.webadmin.upms.model.RefundFeeDetail;
import com.course.app.webadmin.upms.model.StudentFeeDetail;
import com.course.app.webadmin.upms.model.SysClass;
import com.course.app.webadmin.upms.model.SysDorm;
import com.course.app.webadmin.upms.model.SysStudent;
import com.course.app.webadmin.upms.service.ProcessDetailService;
import com.course.app.webadmin.upms.service.RefundFeeDetailService;
import com.course.app.webadmin.upms.service.StudentFeeDetailService;
import com.course.app.webadmin.upms.service.SysClassService;
import com.course.app.webadmin.upms.service.SysDormService;
import com.course.app.webadmin.upms.service.SysStudentService;
import com.varif.app.utils.SendSmsUtil;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;

/**
 * H5移动端登录接口控制器类
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "H5移动端登录接口")
@Slf4j
@RestController
@RequestMapping("/admin/upms/h5Login")
public class H5LoginController extends H5BaseController {

	@Autowired
	private SysStudentService sysStudentService;
	@Autowired
	private IdGeneratorWrapper idGenerator;
	@Autowired
	private RefundFeeDetailService refundFeeDetailService;
	@Autowired
	private StudentFeeDetailService studentFeeDetailService;
	@Autowired
	private SysClassService sysClassService;
	@Autowired
	private SysDormService sysDormService;
	@Autowired
	private ProcessDetailService processDetailService;
	private final static String SMS_CONTENT = "【验证码】：%s。验证码5分钟内有效，请妥善保管。【曲靖一中沾益清源学校】";

	/**
	 * H5端学生注册
	 */
	@NoAuthInterface
	@PostMapping(value = "/register")
	public ResponseResult<Long> register(@MyRequestBody SysStudentDto sysStudentDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(sysStudentDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		SysStudent sysStudent = MyModelUtil.copyTo(sysStudentDto, SysStudent.class);
		sysStudent.setStudentId(idGenerator.nextLongId());
		sysStudent.setRegisterType(1);
		sysStudent.setProcessFlag(0);
		MyModelUtil.fillCommonsForInsert(sysStudent);
		//创建审批日志
		ProcessDetail processDetail = new ProcessDetail();
		processDetail.setProcessId(idGenerator.nextLongId());
		processDetail.setProcessNm("注册审核");
		processDetail.setProcessStep(1);
		processDetail.setOutBusiId(sysStudent.getStudentId());
		processDetail.setApproveId(null);//先
		sysStudentService.save(sysStudent);
		return ResponseResult.success(sysStudent.getStudentId());
	}

	/**
	 * 绑定添加
	 */
	@PostMapping(value = "/addBinding")
	public ResponseResult<Void> addBinding(@MyRequestBody String studentNo, @MyRequestBody String studentName,
			@MyRequestBody String identityCard) {
		if (MyCommonUtil.existBlankArgument(studentNo, studentName, identityCard)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		SysStudent student = sysStudentService
				.getOne(Wrappers.<SysStudent> lambdaQuery().eq(SysStudent::getStudentName, studentName)
						.eq(SysStudent::getIdentityCard, identityCard).eq(SysStudent::getStudentNo, studentNo));
		if (null == student) {
			return ResponseResult.error(ErrorCodeEnum.STUDENT_NOT_EXIST);
		}
		if (-1 == student.getDeletedFlag()) {
			return ResponseResult.error(ErrorCodeEnum.STUDENT_STATUS_ERROR);
		}
		TokenData tokenData = TokenData.takeFromRequest();
		if (StringUtils.isBlank(tokenData.getPhoneNum())) {
			return ResponseResult.error(ErrorCodeEnum.TOKEN_PHONE_NOT_EXIST,
					ErrorCodeEnum.TOKEN_PHONE_NOT_EXIST.getErrorMessage());
		}
		student.setPhoneNum(tokenData.getPhoneNum());
		sysStudentService.updateById(student);
		return ResponseResult.success();
	}

	/**
	 * 绑定移除
	 */
	@PostMapping(value = "/removeBinding")
	public ResponseResult<Void> removeBinding(@MyRequestBody String studentId) {
		if (MyCommonUtil.existBlankArgument(studentId)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		SysStudent student = sysStudentService.getById(studentId);
		if (null == student) {
			return ResponseResult.error(ErrorCodeEnum.STUDENT_NOT_EXIST);
		}
		student.setPhoneNum(StringUtils.EMPTY);
		sysStudentService.updateById(student);
		return ResponseResult.success();
	}

	/**
	 * H5移动端登录验证码发送接口
	 */
	@NoAuthInterface
	@ApiImplicitParams({ @ApiImplicitParam(name = "phoneNum", value = "手机号码", dataType = "String") })
	@PostMapping(value = "/smsCodeSend")
	public ResponseResult<Void> smsCodeSend(@MyRequestBody String phoneNum) throws ClientException {
		log.info("短信发送成功！");
		if (MyCommonUtil.existBlankArgument(phoneNum)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		if (!PhoneUtil.isPhone(phoneNum)) {
			return ResponseResult.error(ErrorCodeEnum.PHONENUM_ERR);
		}
		String smsCode = String.valueOf(RandomUtil.randomInt(100000, 999999));
		String key = RedisKeyUtil.makeRandomCodeH5Key(phoneNum, smsCode);
		String smsContent = String.format(SMS_CONTENT, smsCode);
		log.info("smsCodeSend 短信内容为：{}", smsContent);
		// 短信发送接口
		if (SendSmsUtil.sendShortMessage(phoneNum, smsCode)) {
			ResponseResult.error(ErrorCodeEnum.SMSCODE_SEND_ERR, ErrorCodeEnum.SMSCODE_SEND_ERR.getErrorMessage());
		}
		RBucket<String> cachedData = redissonClient.getBucket(key);
		cachedData.set(smsCode, CacheTime.EXPIR_FIVE_MIN, TimeUnit.SECONDS);
		return ResponseResult.success();
	}

	/**
	 * H5移动端登录接口
	 */
	@NoAuthInterface
	@ApiImplicitParams({ @ApiImplicitParam(name = "studentName", value = "学生姓名", dataType = "String"),
			@ApiImplicitParam(name = "smsCode", value = "验证码", dataType = "String"),
			@ApiImplicitParam(name = "encryptInfo", value = "学生身份证号", dataType = "String"),
			@ApiImplicitParam(name = "phoneNum", value = "手机号码", dataType = "String") })
	@PostMapping(value = "/login")
	public ResponseResult<JSONObject> login(@MyRequestBody String studentName, @MyRequestBody String identityCard,
			@MyRequestBody String phoneNum, @MyRequestBody String smsCode, @MyRequestBody String openId,
			@MyRequestBody String unionId) {
		if (MyCommonUtil.existBlankArgument(studentName, identityCard, phoneNum, smsCode)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		// 手机号码校验
		if (!PhoneUtil.isPhone(phoneNum)) {
			return ResponseResult.error(ErrorCodeEnum.PHONENUM_ERR);
		}
		// 验证码校验
		String key = RedisKeyUtil.makeRandomCodeH5Key(phoneNum, smsCode);
		RBucket<String> cachedData = redissonClient.getBucket(key);
		if (null == cachedData || null == cachedData.get()) {
			return ResponseResult.error(ErrorCodeEnum.SMSCODE_EXPIRE);
		}
		log.info("====>smsCode,{}", cachedData.get());
		if (!StringUtils.equals(smsCode, cachedData.get())) {
			return ResponseResult.error(ErrorCodeEnum.SMSCODE_ERR);
		}
		SysStudent student = sysStudentService.getOne(Wrappers.<SysStudent> lambdaQuery()
				.eq(SysStudent::getStudentName, studentName).eq(SysStudent::getIdentityCard, identityCard));
		if (null == student) {
			return ResponseResult.error(ErrorCodeEnum.STUDENT_NOT_EXIST);
		}
		if (-1 == student.getDeletedFlag()) {
			return ResponseResult.error(ErrorCodeEnum.STUDENT_STATUS_ERROR);
		}
		if (!StringUtils.equals(student.getPhoneNum(), phoneNum)) {
			student.setPhoneNum(phoneNum);
			sysStudentService.updateById(student);
		}
		if (!StringUtils.equals(student.getOpenId(), openId) || !StringUtils.equals(student.getUnionId(), unionId)) {
			if (StringUtils.isNotBlank(openId)) {
				student.setOpenId(openId);
			}
			if (StringUtils.isNotBlank(unionId)) {
				student.setUnionId(unionId);
			}
			sysStudentService.updateById(student);
		}
		JSONObject studentInfo = new JSONObject();
		//处理学生信息
		studentInfo.set("studentId", student.getStudentId());
		studentInfo.set("studentNo", student.getStudentNo());
		studentInfo.set("studentName", student.getStudentName());
		studentInfo.set("sex", SysStudent.convertSex(student.getSex()));
		studentInfo.set("phoneNum", student.getPhoneNum());
		studentInfo.set("identityCard", student.getIdentityCard());
		studentInfo.set("homeAddress", student.getHomeAddress());
		studentInfo.set("domicileAddress", student.getDomicileAddress());
		studentInfo.set("gradeSchool", student.getGradeSchool());
		studentInfo.set("studentStatusDistNm", student.getStudentStatusDistNm());
		//查询班级信息
		SysClass sysClass = sysClassService.getById(student.getClassId());
		if (null != sysClass) {
			studentInfo.set("className", sysClass.getClassName());
			studentInfo.set("classTeacherName", sysClass.getClassTeacherName());
		}
		//查询住宿信息
		SysDorm sysDorm = sysDormService.getById(student.getDormId());
		if (null != sysDorm) {
			studentInfo.set("buildNum", sysDorm.getBuildNum());
			studentInfo.set("dormNum", sysDorm.getDormNum());
			studentInfo.set("dormType", sysDorm.getDormType());
		}
		JSONObject jsonData = initToken(student);
		jsonData.set("studentInfo", studentInfo);
		return ResponseResult.success(jsonData);
	}

	/**
	 * H5端根据手机号码查询绑定的账号信息接口
	 */
	@PostMapping(value = "/listActInfo")
	public ResponseResult<List<JSONObject>> listActInfo(@MyRequestBody String phoneNum,
			@MyRequestBody String studentId) {
		if (MyCommonUtil.existBlankArgument(phoneNum)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		List<SysStudent> sysStudents = sysStudentService.list(Wrappers.<SysStudent> lambdaQuery()
				.eq(SysStudent::getPhoneNum, phoneNum).eq(SysStudent::getDeletedFlag, 1));
		if (CollectionUtils.isEmpty(sysStudents)) {
			return ResponseResult.error(ErrorCodeEnum.STUDENT_NOT_EXIST);
		}
		List<JSONObject> results = new ArrayList<>();
		sysStudents.stream().forEach(e -> {
			JSONObject info = new JSONObject();
			info.set("studentId", e.getStudentId());
			info.set("studentNo", e.getStudentNo());
			info.set("studentName", e.getStudentName());
			info.set("sex", SysStudent.convertSex(e.getSex()));
			info.set("phoneNum", e.getPhoneNum());
			info.set("identityCard", e.getIdentityCard());
			results.add(info);
		});
		return ResponseResult.success(results);
	}

	/**
	 * 小程序openId同步接口
	 */
	@PostMapping(value = "/modifyOpenId")
	public ResponseResult<Void> modifyOpenId(@MyRequestBody String openId, @MyRequestBody String studentId) {
		if (MyCommonUtil.existBlankArgument(openId, studentId)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		SysStudent student = sysStudentService.getById(studentId);
		if (null == student) {
			return ResponseResult.error(ErrorCodeEnum.STUDENT_NOT_EXIST);
		}
		if (!StringUtils.equals(student.getOpenId(), openId)) {
			student.setOpenId(openId);
			sysStudentService.updateById(student);
		}
		return ResponseResult.success();
	}

	/**
	 * 查询当前学生待缴费记录
	 */
	@PostMapping(value = "/queryPendingPayRecords")
	public ResponseResult<List<JSONObject>> queryPendingPayRecords(@MyRequestBody String studentId) {
		if (MyCommonUtil.existBlankArgument(studentId)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		SysStudent student = sysStudentService.getById(studentId);
		if (null == student) {
			return ResponseResult.error(ErrorCodeEnum.STUDENT_NOT_EXIST);
		}
		List<StudentFeeDetail> studentFeeDetails = studentFeeDetailService
				.list(Wrappers.<StudentFeeDetail> lambdaQuery().eq(StudentFeeDetail::getStudentId, studentId)
						.eq(StudentFeeDetail::getFeeStatus, 0).orderByDesc(StudentFeeDetail::getCreateTime));
		List<JSONObject> results = new ArrayList<>();
		studentFeeDetails.stream().forEach(e -> {
			JSONObject jsonObject = JSONUtil.parseObj(e);
			jsonObject.set("feeStatus", StudentFeeDetail.convertFeeStatus(e.getFeeStatus()));
			results.add(jsonObject);
		});
		return ResponseResult.success(results);
	}

	/**
	 * 查询当前学生已缴费记录
	 */
	@PostMapping(value = "/queryPaidPayRecords")
	public ResponseResult<Map<String, Object>> queryPaidPayRecords(@MyRequestBody String studentId,
			@MyRequestBody String year) {
		if (MyCommonUtil.existBlankArgument(studentId, year)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		SysStudent student = sysStudentService.getById(studentId);
		if (null == student) {
			return ResponseResult.error(ErrorCodeEnum.STUDENT_NOT_EXIST);
		}
		List<StudentFeeDetail> studentFeeDetails = studentFeeDetailService
				.list(Wrappers.<StudentFeeDetail> lambdaQuery().eq(StudentFeeDetail::getStudentId, studentId)
						.eq(StudentFeeDetail::getFeeStatus, 1).eq(StudentFeeDetail::getFeeAttrYear, year)
						.orderByDesc(StudentFeeDetail::getCreateTime));
		if (CollectionUtils.isEmpty(studentFeeDetails)) {
			return ResponseResult.success(Collections.emptyMap());
		}
		//已缴费总金额
		double paidPayMoneys = studentFeeDetails.stream()
				.mapToDouble(e -> StringUtils.isEmpty(e.getFeeAmount()) ? 0.00 : Double.valueOf(e.getFeeAmount()))
				.sum();
		//支付记录
		List<Map<String, Object>> details = new ArrayList<>();
		studentFeeDetails.stream().forEach(e -> {
			Map<String, Object> temp = new HashMap<>();
			temp.putAll(JSONUtil.parseObj(e));
			//退费情况
			dealRefundDetail(temp, e);
			details.add(temp);
		});
		Map<String, Object> map = new HashMap<>();
		map.put("detail", details);
		map.put("paidPayMoneys", paidPayMoneys);
		return ResponseResult.success(map);
	}

	/**
	 * 退费信息处理
	 */
	private void dealRefundDetail(Map<String, Object> temp, StudentFeeDetail e) {
		RefundFeeDetail refundFeeDetail = refundFeeDetailService.getOne(Wrappers.<RefundFeeDetail> lambdaQuery()
				.eq(RefundFeeDetail::getStudentId, e.getStudentId()).eq(RefundFeeDetail::getFeeId, e.getFeeId()));
		if (null != refundFeeDetail) {
			//退费金额
			temp.put("refundAmount", refundFeeDetail.getRefundAmount());
			//退费状态
			temp.put("refundStatus", RefundFeeDetail.convertRefundStatus(refundFeeDetail.getRefundStatus()));
		}
	}
}
