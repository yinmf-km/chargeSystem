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
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import com.course.app.webadmin.upms.dto.SchoolFeeDto;
import com.course.app.webadmin.upms.model.SchoolFee;
import com.course.app.webadmin.upms.service.SchoolFeeService;
import com.course.app.webadmin.upms.vo.SchoolFeeVo;
import com.github.pagehelper.page.PageMethod;

import io.swagger.annotations.Api;

/**
 * @Description 学费管理控制器类
 * @author yinmf
 * @Title SysDormController.java
 * @Package com.course.app.webadmin.upms.controller
 * @date 2023年5月3日 下午10:47:22
 * @version V1.0
 */
@Api(tags = "学费管理接口")
@RestController
@RequestMapping("/admin/upms/schoolFee")
public class SchoolFeeController {

	@Autowired
	private SchoolFeeService schoolFeeService;

	@Autowired
	private IdGeneratorWrapper idGenerator;

	/**
	 * 学费信息新增
	 */
	@OperationLog(type = SysOperationLogType.ADD)
	@PostMapping("/add")
	public ResponseResult<Long> add(@MyRequestBody SchoolFeeDto schoolFeeDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(schoolFeeDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		SchoolFee schoolFee = MyModelUtil.copyTo(schoolFeeDto, SchoolFee.class);
		schoolFee.setId(idGenerator.nextLongId());
		MyModelUtil.fillCommonsForInsert(schoolFee);
		schoolFeeService.save(schoolFee);
		return ResponseResult.success(schoolFee.getId());
	}

	/**
	 * 学费信息修改
	 */
	@OperationLog(type = SysOperationLogType.UPDATE)
	@PostMapping("/update")
	public ResponseResult<Void> update(@MyRequestBody SchoolFeeDto schoolFeeDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(schoolFeeDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		if (null == schoolFeeService.getById(schoolFeeDto.getId())) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		SchoolFee schoolFee = MyModelUtil.copyTo(schoolFeeDto, SchoolFee.class);
		MyModelUtil.fillCommonsForInsert(schoolFee);
		schoolFeeService.updateById(schoolFee);
		return ResponseResult.success();
	}

	/**
	 * 学费信息删除
	 */
	@OperationLog(type = SysOperationLogType.DELETE)
	@PostMapping("/delete")
	public ResponseResult<Void> delete(@MyRequestBody Long id) {
		if (MyCommonUtil.existBlankArgument(id)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		if (null == schoolFeeService.getById(id)) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		schoolFeeService.removeById(id);
		return ResponseResult.success();
	}

	/**
	 * 学费信息查询
	 */
	@OperationLog(type = SysOperationLogType.LIST)
	@PostMapping("/list")
	public ResponseResult<MyPageData<SchoolFeeVo>> list(@MyRequestBody SchoolFeeDto schoolFeeDtoFilter, @MyRequestBody MyPageParam pageParam) {
		if (pageParam != null) {
			PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
		}
		SchoolFee schoolFee = MyModelUtil.copyTo(schoolFeeDtoFilter, SchoolFee.class);
		List<SchoolFee> schoolFees = schoolFeeService.getListByFilter(schoolFee);
		return ResponseResult.success(MyPageUtil.makeResponseData(schoolFees, SchoolFee.INSTANCE));
	}
}
