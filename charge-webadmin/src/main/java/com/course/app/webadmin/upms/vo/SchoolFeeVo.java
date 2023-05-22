package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SchoolFeeVO视图对象。
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("SchoolFeeVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class SchoolFeeVo extends BaseVo {

	/**
	 * 主键Id。
	 */
	@ApiModelProperty(value = "主键Id")
	private Long id;
	/**
	 * 生源地。
	 */
	@ApiModelProperty(value = "生源地")
	private String sourceAddress;
	/**
	 * 分数区间。
	 */
	@ApiModelProperty(value = "分数区间")
	private String scoreRange;
	/**
	 * 规则描述。
	 */
	@ApiModelProperty(value = "规则描述")
	private String ruleDesc;
	/**
	 * 费用。
	 */
	@ApiModelProperty(value = "费用")
	private Integer fee;
}
