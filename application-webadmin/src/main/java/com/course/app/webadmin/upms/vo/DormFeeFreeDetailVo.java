package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("DormFeeFreeDetailVo视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DormFeeFreeDetailVo extends BaseVo {

	@ApiModelProperty(value = "主键Id")
	private Long id;

	@ApiModelProperty(value = "学生ID")
	private Long studentId;

	@ApiModelProperty(value = "审核状态(0:审核中 1:同意 2:不同意)")
	private Integer processFlag;
}
