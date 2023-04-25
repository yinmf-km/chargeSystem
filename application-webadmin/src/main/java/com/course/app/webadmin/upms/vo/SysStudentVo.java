package com.course.app.webadmin.upms.vo;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description 学生信息VO
 * @author yinmf
 * @Title SysStudentVo.java
 * @Package com.course.app.webadmin.upms.vo
 * @date 2023年4月24日 下午11:43:06
 * @version V1.0
 */
@ApiModel("SysStudentVo视图对象")
@Data
public class SysStudentVo {

	/**
	 * 学生Id
	 */
	@ApiModelProperty(value = "学生Id")
	private Long studentId;
	/**
	 * 学生名称
	 */
	@ApiModelProperty(value = "学生名称")
	private String studentName;
	/**
	 * 性别(1:男 2:女)
	 */
	@ApiModelProperty(value = "性别(1:男 2:女)")
	private Integer sex;
	/**
	 * 身份证号码
	 */
	@ApiModelProperty(value = "身份证号码")
	private String identityCard;
	/**
	 * 地址
	 */
	@ApiModelProperty(value = "地址")
	private String address;
	/**
	 * 毕业学校
	 */
	@ApiModelProperty(value = "毕业学校")
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
	private String studentStatusDistName;
	/**
	 * 班级
	 */
	@ApiModelProperty(value = "班级名称")
	private Long className;
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
