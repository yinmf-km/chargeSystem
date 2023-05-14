package com.course.app.webadmin.upms.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("SysQuestDto对象")
@Data
public class SysQuestDto {

	@ApiModelProperty(value = "主键Id", required = true)
	@NotNull(message = "主键Id不能为空！", groups = { UpdateGroup.class })
	private Long id;
	@ApiModelProperty(value = "问题名称", required = true)
	@NotBlank(message = "问题名称不能为空！")
	private String questName;
	@ApiModelProperty(value = "问题答案", required = true)
	@NotBlank(message = "问题答案不能为空！")
	private String questAnswer;
}
