package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("SysQuestVo视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysQuestVo extends BaseVo {

	@ApiModelProperty(value = "主键Id")
	private Long id;
	@ApiModelProperty(value = "问题名称")
	private String questName;
	@ApiModelProperty(value = "问题答案")
	private String questAnswer;
}
