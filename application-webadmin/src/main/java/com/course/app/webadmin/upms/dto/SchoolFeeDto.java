package com.course.app.webadmin.upms.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("SchoolFeeDto对象")
@Data
public class SchoolFeeDto {

	@ApiModelProperty(value = "主键Id",required = true)
	@NotNull(message = "主键Id不能为空！",groups = { UpdateGroup.class })
	private Long id;

	@ApiModelProperty(value = "生源地",required = true)
	@NotBlank(message = "生源地不能为空！")
	private String sourceAddress;

	@ApiModelProperty(value = "分数区间",required = true)
	@NotBlank(message = "分数区间不能为空！")
	private String scoreRange;

	@ApiModelProperty(value = "规则描述",required = true)
	@NotBlank(message = "规则描述不能为空！")
	private String ruleDesc;

	@ApiModelProperty(value = "费用",required = true)
	@NotNull(message = "费用不能为空！")
	private Integer fee;
}
