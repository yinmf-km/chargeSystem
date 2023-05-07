package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.List;

/**
 * SysUserVO视图对象。
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("SysUserVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserVo extends BaseVo {

	/**
	 * 用户Id。
	 */
	@ApiModelProperty(value = "用户Id")
	private Long userId;

	/**
	 * 登录用户名。
	 */
	@ApiModelProperty(value = "登录用户名")
	private String loginName;

	/**
	 * 用户部门Id。
	 */
	@ApiModelProperty(value = "用户部门Id")
	private Long deptId;

	/**
	 * 用户显示名称。
	 */
	@ApiModelProperty(value = "用户显示名称")
	private String showName;

	/**
	 * 用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)。
	 */
	@ApiModelProperty(value = "用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)")
	private Integer userType;

	/**
	 * 用户头像的Url。
	 */
	@ApiModelProperty(value = "用户头像的Url")
	private String headImageUrl;

	/**
	 * 用户状态(0: 正常 1: 锁定)。
	 */
	@ApiModelProperty(value = "用户状态(0: 正常 1: 锁定)")
	private Integer userStatus;

	/**
	 * 用户邮箱。
	 */
	@ApiModelProperty(value = "用户邮箱")
	private String email;

	/**
	 * 用户手机。
	 */
	@ApiModelProperty(value = "用户手机")
	private String mobile;

	/**
	 * 多对多用户角色数据集合。
	 */
	@ApiModelProperty(value = "多对多用户角色数据集合")
	private List<Map<String, Object>> sysUserRoleList;

	/**
	 * 多对多用户数据权限数据集合。
	 */
	@ApiModelProperty(value = "多对多用户数据权限数据集合")
	private List<Map<String, Object>> sysDataPermUserList;

	/**
	 * deptId 字典关联数据。
	 */
	@ApiModelProperty(value = "deptId 字典关联数据")
	private Map<String, Object> deptIdDictMap;

	/**
	 * userType 常量字典关联数据。
	 */
	@ApiModelProperty(value = "userType 常量字典关联数据")
	private Map<String, Object> userTypeDictMap;

	/**
	 * userStatus 常量字典关联数据。
	 */
	@ApiModelProperty(value = "userStatus 常量字典关联数据")
	private Map<String, Object> userStatusDictMap;
}
