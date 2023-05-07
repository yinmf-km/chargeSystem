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
import com.course.app.webadmin.upms.dto.SysDormDto;
import com.course.app.webadmin.upms.model.SysDorm;
import com.course.app.webadmin.upms.service.SysDormService;
import com.course.app.webadmin.upms.vo.SysDormVo;
import com.github.pagehelper.page.PageMethod;

import io.swagger.annotations.Api;

/**
 * @Description 宿舍管理控制器类
 * @author yinmf
 * @Title SysDormController.java
 * @Package com.course.app.webadmin.upms.controller
 * @date 2023年5月3日 下午10:47:22
 * @version V1.0
 */
@Api(tags = "宿舍信息管理接口")
@RestController
@RequestMapping("/admin/upms/sysDorm")
public class SysDormController {

	@Autowired
	private SysDormService sysDormService;

	@Autowired
	private IdGeneratorWrapper idGenerator;

	/**
	 * 宿舍信息新增
	 */
	@OperationLog(type = SysOperationLogType.ADD)
	@PostMapping("/add")
	public ResponseResult<Long> add(@MyRequestBody SysDormDto sysDormDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(sysDormDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		SysDorm sysDorm = MyModelUtil.copyTo(sysDormDto, SysDorm.class);
		sysDorm.setDormId(idGenerator.nextLongId());
		MyModelUtil.fillCommonsForInsert(sysDorm);
		sysDormService.save(sysDorm);
		return ResponseResult.success(sysDorm.getDormId());
	}

	/**
	 * 宿舍信息修改
	 */
	@OperationLog(type = SysOperationLogType.UPDATE)
	@PostMapping("/update")
	public ResponseResult<Void> update(@MyRequestBody SysDormDto sysDormDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(sysDormDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		if (null == sysDormService.getById(sysDormDto.getDormId())) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		SysDorm sysDorm = MyModelUtil.copyTo(sysDormDto, SysDorm.class);
		MyModelUtil.fillCommonsForInsert(sysDorm);
		sysDormService.updateById(sysDorm);
		return ResponseResult.success();
	}

	/**
	 * 宿舍信息删除
	 */
	@OperationLog(type = SysOperationLogType.DELETE)
	@PostMapping("/delete")
	public ResponseResult<Void> delete(@MyRequestBody Long dormId) {
		if (MyCommonUtil.existBlankArgument(dormId)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		if (null == sysDormService.getById(dormId)) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		sysDormService.removeById(dormId);
		return ResponseResult.success();
	}

	/**
	 * 宿舍信息查询
	 */
	@OperationLog(type = SysOperationLogType.LIST)
	@PostMapping("/list")
	public ResponseResult<MyPageData<SysDormVo>> list(@MyRequestBody SysDormDto sysDormDtoFilter, @MyRequestBody MyPageParam pageParam) {
		if (pageParam != null) {
			PageMethod.startPage(pageParam.getPageNum(), pageParam.getPageSize());
		}
		SysDorm sysDorm = MyModelUtil.copyTo(sysDormDtoFilter, SysDorm.class);
		List<SysDorm> sysDorms = sysDormService.getListByFilter(sysDorm);
		return ResponseResult.success(MyPageUtil.makeResponseData(sysDorms, SysDorm.INSTANCE));
	}
}
