package com.course.app.webadmin.upms.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("SysStudentDto对象")
@Data
public class SysStudentDto {

	/**
	 * 学生Id
	 */
	@ApiModelProperty(value = "学生Id", required = true)
	@NotNull(message = "数据验证失败，学生Id不能为空！", groups = { UpdateGroup.class })
	private Long studentId;
	/**
	 * 学生名称
	 */
	@ApiModelProperty(value = "学生名称", required = true)
	@NotBlank(message = "数据验证失败，学生名称不能为空！")
	private String studentName;
	/**
	 * 性别(1:男 2:女)
	 */
	@ApiModelProperty(value = "性别(1:男 2:女)", required = true)
	@NotNull(message = "数据验证失败，性别不能为空！")
	private Integer sex;
	/**
	 * 身份证号码
	 */
	@ApiModelProperty(value = "身份证号码", required = true)
	@NotBlank(message = "数据验证失败，身份证号码不能为空！")
	private String identityCard;
	/**
	 * 地址
	 */
	@ApiModelProperty(value = "地址", required = true)
	@NotBlank(message = "数据验证失败，地址不能为空！")
	private String address;
	/**
	 * 毕业学校
	 */
	@ApiModelProperty(value = "毕业学校", required = true)
	@NotBlank(message = "数据验证失败，毕业学校不能为空！")
	private String gradeSchool;
	/**
	 * 中考分数
	 */
	@ApiModelProperty(value = "中考分数")
	private Integer mseScore;
	/**
	 * 学籍
	 */
	@ApiModelProperty(value = "学籍")
	private String studentStatusDistId;
	/**
	 * 班级
	 */
	@ApiModelProperty(value = "班级")
	private Long classId;
	/**
	 * 住宿类型
	 */
	@ApiModelProperty(value = "住宿类型")
	private String dormType;
	/**
	 * 宿舍号
	 */
	@ApiModelProperty(value = "宿舍号")
	private String dormCode;
	/**
	 * 是否贫困生(0:不是 1:是)
	 */
	@ApiModelProperty(value = "是否贫困生(0:不是 1:是)")
	private Boolean poorFlag;
	/**
	 * 抚养人
	 */
	@ApiModelProperty(value = "抚养人")
	private String dependant;
	/**
	 * 学费支付方式(1:现金 2:银行卡 3:微信 4:支付宝)
	 */
	@ApiModelProperty(value = "学费支付方式(1:现金 2:银行卡 3:微信 4:支付宝)")
	private Integer payType;
}
