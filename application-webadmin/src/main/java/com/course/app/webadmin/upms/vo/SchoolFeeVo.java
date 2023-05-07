package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("SchoolFeeVo视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class SchoolFeeVo extends BaseVo {

	@ApiModelProperty(value = "主键Id")
	private Long id;

	@ApiModelProperty(value = "生源地")
	private String sourceAddress;

	@ApiModelProperty(value = "分数区间")
	private String scoreRange;

	@ApiModelProperty(value = "规则描述")
	private String ruleDesc;

	@ApiModelProperty(value = "费用")
	private Integer fee;
}
