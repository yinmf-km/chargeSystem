package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CollegeScoreVO视图对象。
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("CollegeScoreVO视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class CollegeScoreVo extends BaseVo {

	/**
	 * 主键Id。
	 */
	@ApiModelProperty(value = "主键Id")
	private Long id;
	/**
	 * 学生名称。
	 */
	@ApiModelProperty(value = "学生名称")
	private String studentName;
	/**
	 * 性别(1:男 2:女)。
	 */
	@ApiModelProperty(value = "性别(1:男 2:女)")
	private Integer sex;
	/**
	 * 身份证号码。
	 */
	@ApiModelProperty(value = "身份证号码")
	private String identityCard;
	/**
	 * 地址。
	 */
	@ApiModelProperty(value = "地址")
	private String address;
	/**
	 * 毕业学校。
	 */
	@ApiModelProperty(value = "毕业学校")
	private String gradeSchool;
	/**
	 * 中考分数。
	 */
	@ApiModelProperty(value = "中考分数")
	private Integer mseScore;
	/**
	 * 学籍。
	 */
	@ApiModelProperty(value = "学籍")
	private String studentStatusDistId;
	/**
	 * 审核状态(0:审核中 1:同意 2:不同意)。
	 */
	@ApiModelProperty(value = "审核状态(0:审核中 1:同意 2:不同意)")
	private Integer processFlag;
	/**
	 * 当前审批人员Id。
	 */
	@ApiModelProperty(value = "当前审批人员Id")
	private Long approveId;
	/**
	 * 审批时间。
	 */
	@ApiModelProperty(value = "审批时间")
	private Long approveTime;
}
