package com.course.app.webadmin.upms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.object.MyPageData;
import com.course.app.common.core.object.ResponseResult;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.core.util.MyPageUtil;
import com.course.app.common.log.annotation.OperationLog;
import com.course.app.common.log.model.constant.SysOperationLogType;
import com.course.app.common.sequence.wrapper.IdGeneratorWrapper;
import com.course.app.webadmin.upms.dto.SysQuestDto;
import com.course.app.webadmin.upms.model.SysQuest;
import com.course.app.webadmin.upms.service.SysQuestService;
import com.course.app.webadmin.upms.vo.SysQuestVo;

import io.swagger.annotations.Api;

/**
 * @Description 问题控制器
 * @author 尹孟飞 E-mail：yinmf@asiainfo.com
 * @Title SysQuestController.java
 * @Package com.course.app.webadmin.upms.controller
 * @date 2023年5月8日 下午11:06:37
 * @version V1.0
 */
@Api(tags = "系统问题管理接口")
@RestController
@RequestMapping("/admin/upms/sysQuest")
public class SysQuestController {

	@Autowired
	private SysQuestService sysQuestService;
	@Autowired
	private IdGeneratorWrapper idGenerator;

	/**
	 * 系统问题新增
	 */
	@OperationLog(type = SysOperationLogType.ADD)
	@PostMapping("/add")
	public ResponseResult<Long> add(@MyRequestBody SysQuestDto sysQuestDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(sysQuestDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		SysQuest sysQuest = MyModelUtil.copyTo(sysQuestDto, SysQuest.class);
		sysQuest.setId(idGenerator.nextLongId());
		MyModelUtil.fillCommonsForInsert(sysQuest);
		sysQuestService.save(sysQuest);
		return ResponseResult.success(sysQuest.getId());
	}

	/**
	 * 系统问题修改
	 */
	@OperationLog(type = SysOperationLogType.UPDATE)
	@PostMapping("/update")
	public ResponseResult<Void> update(@MyRequestBody SysQuestDto sysQuestDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(sysQuestDto, false);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		if (null == sysQuestService.getById(sysQuestDto.getId())) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		SysQuest sysQuest = MyModelUtil.copyTo(sysQuestDto, SysQuest.class);
		MyModelUtil.fillCommonsForInsert(sysQuest);
		sysQuestService.updateById(sysQuest);
		return ResponseResult.success();
	}

	/**
	 * 系统问题删除
	 */
	@OperationLog(type = SysOperationLogType.DELETE)
	@PostMapping("/delete")
	public ResponseResult<Void> delete(@MyRequestBody Long id) {
		if (MyCommonUtil.existBlankArgument(id)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		if (null == sysQuestService.getById(id)) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		sysQuestService.removeById(id);
		return ResponseResult.success();
	}

	/**
	 * 系统问题查询
	 */
	@OperationLog(type = SysOperationLogType.LIST)
	@PostMapping("/list")
	public ResponseResult<MyPageData<SysQuestVo>> list() {
		List<SysQuest> sysQuests = sysQuestService
				.list(Wrappers.<SysQuest> lambdaQuery().eq(SysQuest::getDeletedFlag, 1));
		return ResponseResult.success(MyPageUtil.makeResponseData(sysQuests, SysQuest.INSTANCE));
	}
}
