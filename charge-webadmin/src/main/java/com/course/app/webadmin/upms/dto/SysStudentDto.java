package com.course.app.webadmin.upms.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * SysStudentDto对象。
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("SysStudentDto对象")
@Data
public class SysStudentDto {

	/**
	 * 主键Id。
	 */
	@ApiModelProperty(value = "主键Id", required = true)
	@NotNull(message = "数据验证失败，主键Id不能为空！", groups = { UpdateGroup.class })
	private Long studentId;
	/**
	 * 学生学号。
	 */
	@ApiModelProperty(value = "学生学号", required = true)
	@NotBlank(message = "数据验证失败，学生学号不能为空！")
	private String studentNo;
	/**
	 * 学生名称。
	 */
	@ApiModelProperty(value = "学生名称", required = true)
	@NotBlank(message = "数据验证失败，学生名称不能为空！")
	private String studentName;
	/**
	 * 性别(1:男 2:女)。
	 */
	@ApiModelProperty(value = "性别(1:男 2:女)", required = true)
	@NotNull(message = "数据验证失败，性别(1:男 2:女)不能为空！")
	private Integer sex;
	/**
	 * 身份证号码。
	 */
	@ApiModelProperty(value = "身份证号码", required = true)
	@NotBlank(message = "数据验证失败，身份证号码不能为空！")
	private String identityCard;
	/**
	 * 家庭地址。
	 */
	@ApiModelProperty(value = "家庭地址")
	private String homeAddress;
	/**
	 * 户籍地址。
	 */
	@ApiModelProperty(value = "户籍地址")
	private String domicileAddress;
	/**
	 * 毕业学校。
	 */
	@ApiModelProperty(value = "毕业学校", required = true)
	@NotBlank(message = "数据验证失败，毕业学校不能为空！")
	private String gradeSchool;
	/**
	 * 中考分数。
	 */
	@ApiModelProperty(value = "中考分数")
	private Integer mseScore;
	/**
	 * 生源地名称。
	 */
	@ApiModelProperty(value = "生源地名称")
	private String studentStatusDistNm;
	/**
	 * 宿舍Id。
	 */
	@ApiModelProperty(value = "宿舍Id")
	private Long dormId;
	/**
	 * 班级。
	 */
	@ApiModelProperty(value = "班级")
	private Long classId;
	/**
	 * 是否贫困生(0:不是 1:是)。
	 */
	@ApiModelProperty(value = "是否贫困生(0:不是 1:是)")
	private Integer poorFlag;
	/**
	 * 抚养人。
	 */
	@ApiModelProperty(value = "抚养人")
	private String dependant;
	/**
	 * 学生绑定手机号码。
	 */
	@ApiModelProperty(value = "学生绑定手机号码", required = true)
	//@NotBlank(message = "数据验证失败，学生绑定手机号码不能为空！")
	private String phoneNum;
	/**
	 * 学费支付方式(1:现金 2:银行卡 3:微信 4:支付宝)。
	 */
	@ApiModelProperty(value = "学费支付方式(1:现金 2:银行卡 3:微信 4:支付宝)")
	private Integer payType;
	/**
	 * 政治面貌。
	 */
	@ApiModelProperty(value = "政治面貌")
	private String politicalOutlook;
	/**
	 * 学籍类型(1:统招 2:民办 3:借读)。
	 */
	@ApiModelProperty(value = "学籍类型(1:统招 2:民办 3:借读)")
	private Integer statusType;
	/**
	 * 家长备注。
	 */
	@ApiModelProperty(value = "家长备注")
	private String parentNotes;
	/**
	 * 学籍状态(1:入学 2:休学 3:退学)。
	 */
	@ApiModelProperty(value = "学籍状态(1:入学 2:休学 3:退学)")
	private Integer studentStatus;
	/**
	 * 入学时间。
	 */
	@ApiModelProperty(value = "入学时间")
	private Date enrolTime;
	/**
	 * 退学时间。
	 */
	@ApiModelProperty(value = "退学时间")
	private Date dropoutTime;
	/**
	 * 休学时间。
	 */
	@ApiModelProperty(value = "休学时间")
	private Date ostTime;
	/**
	 * 注册来源(1:手机端 2:PC)。
	 */
	@ApiModelProperty(value = "注册来源(1:手机端 2:PC)")
	private Integer registerType;
	/**
	 * 微信小程序openId。
	 */
	@ApiModelProperty(value = "微信小程序openId")
	private String openId;
	/**
	 * 微信公众号UnionID
	 */
	@ApiModelProperty(value = "微信公众号UnionID")
	private String unionId;
}
