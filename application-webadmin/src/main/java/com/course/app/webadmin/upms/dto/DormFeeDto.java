package com.course.app.webadmin.upms.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("DormFeeDto对象")
@Data
public class DormFeeDto {

	@ApiModelProperty(value = "主键Id",required = true)
	@NotNull(message = "主键Id不能为空！",groups = { UpdateGroup.class })
	private Long id;

	@ApiModelProperty(value = "宿舍类型",required = true)
	@NotBlank(message = "宿舍类型不能为空！")
	private String dormType;

	@ApiModelProperty(value = "费用",required = true)
	@NotNull(message = "费用不能为空！")
	private Integer fee;
}
