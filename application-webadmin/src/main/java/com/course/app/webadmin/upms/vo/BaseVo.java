package com.course.app.webadmin.upms.vo;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseVo {

	/**
	 * 创建者Id。
	 */
	@ApiModelProperty(value = "创建者Id")
	private Long createUserId;

	/**
	 * 更新者Id。
	 */
	@ApiModelProperty(value = "更新者Id")
	private Long updateUserId;

	/**
	 * 创建时间。
	 */
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 更新时间。
	 */
	@ApiModelProperty(value = "更新时间")
	private Date updateTime;
}
