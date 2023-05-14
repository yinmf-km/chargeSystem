package com.course.app.webadmin.upms.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.groups.Default;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.course.app.common.core.annotation.MyRequestBody;
import com.course.app.common.core.constant.ErrorCodeEnum;
import com.course.app.common.core.object.ResponseResult;
import com.course.app.common.core.util.MyCommonUtil;
import com.course.app.common.core.util.MyModelUtil;
import com.course.app.common.core.validator.UpdateGroup;
import com.course.app.common.log.annotation.OperationLog;
import com.course.app.common.log.model.constant.SysOperationLogType;
import com.course.app.webadmin.upms.dto.SysPermModuleDto;
import com.course.app.webadmin.upms.model.SysPerm;
import com.course.app.webadmin.upms.model.SysPermModule;
import com.course.app.webadmin.upms.service.SysPermModuleService;
import com.course.app.webadmin.upms.vo.SysPermModuleVo;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * 权限资源模块管理接口控制器类。
 * @author 云翼
 * @date 2023-02-21
 */
@Api(tags = "权限资源模块管理接口")
@Slf4j
@RestController
@RequestMapping("/admin/upms/sysPermModule")
public class SysPermModuleController {

	@Autowired
	private SysPermModuleService sysPermModuleService;

	/**
	 * 新增权限资源模块操作。
	 * @param sysPermModuleDto 新增权限资源模块对象。
	 * @return 应答结果对象，包含新增权限资源模块的主键Id。
	 */
	@ApiOperationSupport(ignoreParameters = { "sysPermModuleDto.moduleId" })
	@OperationLog(type = SysOperationLogType.ADD)
	@PostMapping("/add")
	public ResponseResult<Long> add(@MyRequestBody SysPermModuleDto sysPermModuleDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(sysPermModuleDto);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		SysPermModule sysPermModule = MyModelUtil.copyTo(sysPermModuleDto, SysPermModule.class);
		if (sysPermModule.getParentId() != null && sysPermModuleService.getById(sysPermModule.getParentId()) == null) {
			errorMessage = "数据验证失败，关联的上级权限模块并不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_PARENT_ID_NOT_EXIST, errorMessage);
		}
		sysPermModuleService.saveNew(sysPermModule);
		return ResponseResult.success(sysPermModule.getModuleId());
	}

	/**
	 * 更新权限资源模块操作。
	 * @param sysPermModuleDto 更新权限资源模块对象。
	 * @return 应答结果对象，包含新增权限资源模块的主键Id。
	 */
	@OperationLog(type = SysOperationLogType.UPDATE)
	@PostMapping("/update")
	public ResponseResult<Void> update(@MyRequestBody SysPermModuleDto sysPermModuleDto) {
		String errorMessage = MyCommonUtil.getModelValidationError(sysPermModuleDto, Default.class, UpdateGroup.class);
		if (errorMessage != null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_VALIDATED_FAILED, errorMessage);
		}
		SysPermModule sysPermModule = MyModelUtil.copyTo(sysPermModuleDto, SysPermModule.class);
		SysPermModule originalPermModule = sysPermModuleService.getById(sysPermModule.getModuleId());
		if (originalPermModule == null) {
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST);
		}
		if (sysPermModule.getParentId() != null && !sysPermModule.getParentId().equals(originalPermModule.getParentId()) && sysPermModuleService.getById(sysPermModule.getParentId()) == null) {
			errorMessage = "数据验证失败，关联的上级权限模块并不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_PARENT_ID_NOT_EXIST, errorMessage);
		}
		if (!sysPermModuleService.update(sysPermModule, originalPermModule)) {
			errorMessage = "数据验证失败，当前模块并不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
		}
		return ResponseResult.success();
	}

	/**
	 * 删除指定权限资源模块操作。
	 * @param moduleId 指定的权限资源模块主键Id。
	 * @return 应答结果对象。
	 */
	@OperationLog(type = SysOperationLogType.DELETE)
	@PostMapping("/delete")
	public ResponseResult<Void> delete(@MyRequestBody Long moduleId) {
		if (MyCommonUtil.existBlankArgument(moduleId)) {
			return ResponseResult.error(ErrorCodeEnum.ARGUMENT_NULL_EXIST);
		}
		String errorMessage;
		if (sysPermModuleService.hasChildren(moduleId) || sysPermModuleService.hasModulePerms(moduleId)) {
			errorMessage = "数据验证失败，当前权限模块存在子模块或权限资源，请先删除关联数据！";
			return ResponseResult.error(ErrorCodeEnum.HAS_CHILDREN_DATA, errorMessage);
		}
		if (!sysPermModuleService.remove(moduleId)) {
			errorMessage = "数据操作失败，权限模块不存在，请刷新后重试！";
			return ResponseResult.error(ErrorCodeEnum.DATA_NOT_EXIST, errorMessage);
		}
		return ResponseResult.success();
	}

	/**
	 * 查看全部权限资源模块列表。
	 * @return 应答结果对象，包含权限资源模块列表。
	 */
	@PostMapping("/list")
	public ResponseResult<List<SysPermModuleVo>> list() {
		List<SysPermModule> permModuleList = sysPermModuleService.getAllListByOrder("showOrder");
		return ResponseResult.success(MyModelUtil.copyCollectionTo(permModuleList, SysPermModuleVo.class));
	}

	/**
	 * 列出全部权限资源模块及其下级关联的权限资源列表。
	 * @return 应答结果对象，包含树状列表，结构为权限资源模块和权限资源之间的树状关系。
	 */
	@PostMapping("/listAll")
	public ResponseResult<List<Map<String, Object>>> listAll() {
		List<SysPermModule> sysPermModuleList = sysPermModuleService.getPermModuleAndPermList();
		List<Map<String, Object>> resultList = new LinkedList<>();
		for (SysPermModule sysPermModule : sysPermModuleList) {
			Map<String, Object> permModuleMap = new HashMap<>(5);
			permModuleMap.put("id", sysPermModule.getModuleId());
			permModuleMap.put("name", sysPermModule.getModuleName());
			permModuleMap.put("type", sysPermModule.getModuleType());
			permModuleMap.put("isPerm", false);
			if (MyCommonUtil.isNotBlankOrNull(sysPermModule.getParentId())) {
				permModuleMap.put("parentId", sysPermModule.getParentId());
			}
			resultList.add(permModuleMap);
			if (CollectionUtils.isNotEmpty(sysPermModule.getSysPermList())) {
				for (SysPerm sysPerm : sysPermModule.getSysPermList()) {
					Map<String, Object> permMap = new HashMap<>(4);
					permMap.put("id", sysPerm.getPermId());
					permMap.put("name", sysPerm.getPermName());
					permMap.put("isPerm", true);
					permMap.put("url", sysPerm.getUrl());
					permMap.put("parentId", sysPermModule.getModuleId());
					resultList.add(permMap);
				}
			}
		}
		return ResponseResult.success(resultList);
	}
}
