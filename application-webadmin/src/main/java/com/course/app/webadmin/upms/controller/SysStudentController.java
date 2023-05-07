package com.course.app.webadmin.upms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.object.MyPageData;
import com.course.app.common.core.object.MyPageParam;
import com.course.app.common.core.object.ResponseResult;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.core.util.MyPageUtil;
import com.course.app.common.log.annotation.OperationLog;
import com.course.app.common.log.model.constant.SysOperationLogType;
import com.course.app.webadmin.upms.dto.SysStudentDto;
import com.course.app.webadmin.upms.model.SysStudent;
import com.course.app.webadmin.upms.service.SysStudentService;
import com.course.app.webadmin.upms.vo.SysStudentVo;
import com.github.pagehelper.page.PageMethod;

import io.swagger.annotations.Api;

/**
 * @Description 学生管理控制器类
 * @author yinmf
 * @Title SysStudentController.java
 * @Package com.course.app.webadmin.upms.controller
 * @date 2023年5月3日 下午9:03:32
 * @version V1.0
 */
@Api(tags = "学生管理管理接口")
@RestController
@RequestMapping("/admin/upms/sysStudent")
public class SysStudentController {

	@Autowired
	private SysStudentService sysStudentService;

	/**
	 * 学生新增
	 */
	@OperationLog(type = SysOperationLogType.ADD)
	@PostMapping("/add")
	public ResponseResult<String> add(@MyRequestBody SysStudentDto sysStudentDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(sysStudentDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		SysStudent sysStudent = MyModelUtil.copyTo(sysStudentDto, SysStudent.class);
		MyModelUtil.fillCommonsForInsert(sysStudent);
		sysStudentService.save(sysStudent);
		return ResponseResult.success(sysStudent.getStudentNo());
	}

	/**
	 * 学生更新
	 */
	@OperationLog(type = SysOperationLogType.UPDATE)
	@PostMapping("/update")
	public ResponseResult<Void> update(@MyRequestBody SysStudentDto sysStudentDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(sysStudentDto, true);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		if (null == sysStudentService.getById(sysStudentDto.getStudentNo())) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		SysStudent studentNew = MyModelUtil.copyTo(sysStudentDto, SysStudent.class);
		MyModelUtil.fillCommonsForInsert(studentNew);
		sysStudentService.updateById(studentNew);
		return ResponseResult.success();
	}

	/**
	 * 学生删除
	 */
	@OperationLog(type = SysOperationLogType.DELETE)
	@PostMapping("/delete")
	public ResponseResult<Void> delete(@MyRequestBody Long userId) {
		if (MyCommonUtil.existBlankArgument(userId)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		if (null == sysStudentService.getById(userId)) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		sysStudentService.removeById(userId);
		return ResponseResult.success();
	}

	/**
	 * 学生查询
	 */
	@OperationLog(type = SysOperationLogType.LIST)
	@PostMapping("/list")
	public ResponseResult<MyPageData<SysStudentVo>> list(@MyRequestBody SysStudentDto sysStudentDtoFilter, @MyRequestBody MyPageParam pageParam) {
		if (pageParam != null) {
			PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
		}
		SysStudent sysStudent = MyModelUtil.copyTo(sysStudentDtoFilter, SysStudent.class);
		List<SysStudent> sysStudents = sysStudentService.getListByFilter(sysStudent);
		return ResponseResult.success(MyPageUtil.makeResponseData(sysStudents, SysStudent.INSTANCE));
	}
}
