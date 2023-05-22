package com.course.app.webadmin.upms.model;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.webadmin.upms.vo.SysStudentVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SysStudent实体对象。
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_student")
public class SysStudent extends BaseModel {

	/**
	 * 主键Id。
	 */
	@TableId(value = "student_id")
	private Long studentId;
	/**
	 * 学生学号。
	 */
	private String studentNo;
	/**
	 * 学生名称。
	 */
	private String studentName;
	/**
	 * 性别(1:男 2:女)。
	 */
	private Integer sex;
	/**
	 * 身份证号码。
	 */
	private String identityCard;
	/**
	 * 家庭地址。
	 */
	private String homeAddress;
	/**
	 * 户籍地址。
	 */
	private String domicileAddress;
	/**
	 * 毕业学校。
	 */
	private String gradeSchool;
	/**
	 * 中考分数。
	 */
	private Integer mseScore;
	/**
	 * 生源地名称。
	 */
	private String studentStatusDistNm;
	/**
	 * 宿舍Id。
	 */
	private Long dormId;
	/**
	 * 班级。
	 */
	private Long classId;
	/**
	 * 是否贫困生(0:不是 1:是)。
	 */
	private Integer poorFlag;
	/**
	 * 抚养人。
	 */
	private String dependant;
	/**
	 * 学生绑定手机号码。
	 */
	private String phoneNum;
	/**
	 * 学费支付方式(1:现金 2:银行卡 3:微信 4:支付宝)。
	 */
	private Integer payType;
	/**
	 * 逻辑删除标记字段(1: 正常 -1: 已删除)。
	 */
	@TableLogic
	private Integer deletedFlag;
	/**
	 * 政治面貌。
	 */
	private String politicalOutlook;
	/**
	 * 学籍类型(1:统招 2:民办 3:借读)。
	 */
	private Integer statusType;
	/**
	 * 家长备注。
	 */
	private String parentNotes;
	/**
	 * 学籍状态(1:入学 2:休学 3:退学)。
	 */
	private Integer studentStatus;
	/**
	 * 注册审核状态(0:待审核 1:同意 2:拒绝)
	 */
	private Integer processFlag;
	/**
	 * 入学时间。
	 */
	private Date enrolTime;
	/**
	 * 退学时间。
	 */
	private Date dropoutTime;
	/**
	 * 休学时间。
	 */
	private Date ostTime;
	/**
	 * 注册来源(1:手机端 2:PC)。
	 */
	private Integer registerType;
	/**
	 * 微信小程序openId。
	 */
	private String openId;
	/**
	 * 微信公众号UnionID
	 */
	private String unionId;

	@Mapper
	public interface SysStudentModelMapper extends BaseModelMapper<SysStudentVo, SysStudent> {
	}

	public static final SysStudentModelMapper INSTANCE = Mappers.getMapper(SysStudentModelMapper.class);

	public static String convertSex(Integer sex) {
		switch (sex) {
		case 1:
			return "男";
		case 2:
			return "女";
		default:
			return StringUtils.EMPTY;
		}
	}
}
