package com.course.app.webadmin.upms.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("SysDormDto对象")
@Data
public class SysDormDto {

	@ApiModelProperty(value = "宿舍Id",required = true)
	@NotNull(message = "宿舍Id不能为空！",groups = { UpdateGroup.class })
	private Long dormId;

	@ApiModelProperty(value = "楼栋号",required = true)
	@NotBlank(message = "楼栋号不能为空！")
	private String buildNum;

	@ApiModelProperty(value = "宿舍编号",required = true)
	@NotBlank(message = "宿舍编号不能为空！")
	private String dormNum;

	@ApiModelProperty(value = "宿舍类型",required = true)
	@NotBlank(message = "宿舍类型不能为空！")
	private String dormType;

	@ApiModelProperty(value = "床位数",required = true)
	@NotNull(message = "床位数不能为空！")
	private Integer bedNum;

	@ApiModelProperty(value = "费用规格(元/学期)",required = true)
	@NotNull(message = "费用规格(元/学期)不能为空！")
	private Integer fee;
}
