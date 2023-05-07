package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("SysDormVo视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDormVo extends BaseVo {

	@ApiModelProperty(value = "宿舍Id")
	private Long dormId;

	@ApiModelProperty(value = "楼栋号")
	private String buildNum;

	@ApiModelProperty(value = "宿舍编号")
	private String dormNum;

	@ApiModelProperty(value = "宿舍类型")
	private String dormType;

	@ApiModelProperty(value = "床位数")
	private Integer bedNum;

	@ApiModelProperty(value = "费用规格(元/学期)")
	private Integer fee;
}
