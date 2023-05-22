package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SysDormVO视图对象。
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("SysDormVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDormVo extends BaseVo {

	/**
	 * 主键Id。
	 */
	@ApiModelProperty(value = "主键Id")
	private Long dormId;
	/**
	 * 楼栋号。
	 */
	@ApiModelProperty(value = "楼栋号")
	private String buildNum;
	/**
	 * 宿舍编号。
	 */
	@ApiModelProperty(value = "宿舍编号")
	private String dormNum;
	/**
	 * 宿舍类型。
	 */
	@ApiModelProperty(value = "宿舍类型")
	private String dormType;
	/**
	 * 床位数。
	 */
	@ApiModelProperty(value = "床位数")
	private Integer bedNum;
	/**
	 * 费用规格(元/学期)。
	 */
	@ApiModelProperty(value = "费用规格(元/学期)")
	private Integer fee;
	/**
	 * 是否满房(1: 是 -1: 否)。
	 */
	@ApiModelProperty(value = "是否满房(1: 是 -1: 否)")
	private Integer isFull;
}
