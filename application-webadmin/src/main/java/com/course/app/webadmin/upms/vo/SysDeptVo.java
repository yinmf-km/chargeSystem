package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SysDeptVO视图对象。
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("SysDeptVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDeptVo extends BaseVo {

	/**
	 * 部门Id。
	 */
	@ApiModelProperty(value = "部门Id")
	private Long deptId;

	/**
	 * 部门名称。
	 */
	@ApiModelProperty(value = "部门名称")
	private String deptName;

	/**
	 * 显示顺序。
	 */
	@ApiModelProperty(value = "显示顺序")
	private Integer showOrder;

	/**
	 * 父部门Id。
	 */
	@ApiModelProperty(value = "父部门Id")
	private Long parentId;
}
