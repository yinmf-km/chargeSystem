package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * DormFeeVO视图对象。
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("DormFeeVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class DormFeeVo extends BaseVo {

	/**
	 * 主键Id。
	 */
	@ApiModelProperty(value = "主键Id")
	private Long id;
	/**
	 * 宿舍类型。
	 */
	@ApiModelProperty(value = "宿舍类型")
	private String dormType;
	/**
	 * 费用。
	 */
	@ApiModelProperty(value = "费用")
	private Integer fee;
}
