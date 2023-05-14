package com.course.app.webadmin.upms.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.core.annotation.NoAuthInterface;
import com.course.app.common.core.constant.ComConstant.CacheTime;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.object.ResponseResult;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.core.util.RedisKeyUtil;
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import com.course.app.webadmin.upms.dto.SysStudentDto;
import com.course.app.webadmin.upms.model.SysStudent;
import com.course.app.webadmin.upms.service.SysClassService;
import com.course.app.webadmin.upms.service.SysStudentService;
import com.course.app.webadmin.upms.vo.SysStudentVo;

import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.RandomUtil;
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
public class H5LoginController {

    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private SysStudentService sysStudentService;
    @Autowired
    private SysClassService sysClassService;
    @Autowired
    private IdGeneratorWrapper idGenerator;
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
        MyModelUtil.fillCommonsForInsert(sysStudent);
        sysStudentService.save(sysStudent);
        return ResponseResult.success(sysStudent.getStudentId());
    }

    /**
     * 
     * @Method smsCodeSend
     * @Return ResponseResult<Void>
     * @Param  
     * @throws 
     * @Description H5移动端登录验证码发送接口
     * @Title  H5LoginController.java
     * @Package com.course.app.webadmin.upms.controller
     * @date 2023年5月13日 下午10:05:54
     * @version V1.0
     */
    @NoAuthInterface
    @ApiImplicitParams({@ApiImplicitParam(name = "phoneNum", value = "手机号码", dataType = "String")})
    @PostMapping(value = "/smsCodeSend")
    public ResponseResult<Void> smsCodeSend(@MyRequestBody String phoneNum) {
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
        RBucket<String> cachedData = redissonClient.getBucket(key);
        cachedData.set(smsCode, CacheTime.EXPIR_FIVE_MIN, TimeUnit.SECONDS);
        return ResponseResult.success();
    }

    /**
     * 
     * @Method login
     * @Return ResponseResult<SysStudentVo>
     * @Param  
     * @throws 
     * @Description H5移动端登录接口
     */
    @NoAuthInterface
    @ApiImplicitParams({@ApiImplicitParam(name = "userName", value = "学生姓名", dataType = "String"),
        @ApiImplicitParam(name = "smsCode", value = "验证码", dataType = "String"),
        @ApiImplicitParam(name = "encryptInfo", value = "学生身份证号。需要加密，加密方式另外讨论",
            dataType = "String"),
        @ApiImplicitParam(name = "phoneNum", value = "手机号码", dataType = "String")})
    @PostMapping(value = "/login")
    public ResponseResult<SysStudentVo> login(@MyRequestBody String userName,
        @MyRequestBody String encryptInfo, @MyRequestBody String phoneNum,
        @MyRequestBody String smsCode) {
        if (MyCommonUtil.existBlankArgument(userName, encryptInfo, phoneNum, smsCode)) {
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
        // 身份证号码解密校验
        String identityCard = encryptInfo;
        SysStudent student = sysStudentService
            .getOne(Wrappers.<SysStudent>lambdaQuery().eq(SysStudent::getStudentName, userName)
                .eq(SysStudent::getIdentityCard, identityCard));
        if (null == student) {
            return ResponseResult.error(ErrorCodeEnum.STUDENT_NOT_EXIST);
        }
        if (-1 == student.getDeletedFlag()) {
            return ResponseResult.error(ErrorCodeEnum.STUDENT_STATUS_ERROR);
        }
        SysStudentVo sysStudentVo = MyModelUtil.copyTo(student, SysStudentVo.class);
        return ResponseResult.success(sysStudentVo);
    }

    /**
     * 
     * @Method listActInfo
     * @Return ResponseResult<List<SysStudent>>
     * @Param  
     * @throws 
     * @Description H5端根据手机号码查询绑定的账号信息接口
     */
    @NoAuthInterface
    @PostMapping(value = "/listActInfo")
    public ResponseResult<List<SysStudent>> listActInfo(@MyRequestBody String phoneNum) {
        if (MyCommonUtil.existBlankArgument(phoneNum)) {
            return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
        }
        Map<String, Object> condition = new HashMap<>();
        condition.put("phoneNum", phoneNum);
        condition.put("deletedFlag", 1);
        // sysClassService.list(Wrappers.<SysClass>lambdaQuery().eq(SysClass::getClassName, "高一"));
        List<SysStudent> sysStudents = sysStudentService.list(Wrappers.<SysStudent>lambdaQuery()
            .eq(SysStudent::getPhoneNum, phoneNum).eq(SysStudent::getDeletedFlag, 1));
        // listStudentInfo(condition);
        return ResponseResult.success(sysStudents);
        /* return ResponseResult.success(
            sysStudents.stream().map(MapUtil::toCamelCaseMap).collect(Collectors.toList()));*/
    }
}
