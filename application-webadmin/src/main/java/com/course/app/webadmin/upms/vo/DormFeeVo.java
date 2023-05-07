package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel("DormFeeVo视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DormFeeVo extends BaseVo {

	@ApiModelProperty(value = "主键Id")
	private Long id;

	@ApiModelProperty(value = "宿舍类型")
	private String dorm_type;

	@ApiModelProperty(value = "费用")
	private Integer fee;
}
