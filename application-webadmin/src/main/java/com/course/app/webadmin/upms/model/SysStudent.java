package com.course.app.webadmin.upms.model;

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
 * @Description 学生实体对象
 * @author yinmf
 * @Title SysStudent.java
 * @Package com.course.app.webadmin.upms.model
 * @date 2023年4月24日 下午11:31:01
 * @version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_student")
public class SysStudent extends BaseModel {

	/**
	 * 学号
	 */
	@TableId(value = "student_no")
	private String studentNo;

	/**
	 * 学生名称
	 */
	private String studentName;

	/**
	 * 性别(1:男 2:女)
	 */
	private Integer sex;

	/**
	 * 身份证号码
	 */
	private String identityCard;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 毕业学校
	 */
	private String gradeSchool;

	/**
	 * 中考分数
	 */
	private Integer mseScore;

	/**
	 * 学籍
	 */
	private String studentStatusDistId;

	/**
	 * 班级
	 */
	private Long classId;

	/**
	 * 住宿类型
	 */
	private String dormType;

	/**
	 * 宿舍号
	 */
	private String dormCode;

	/**
	 * 是否贫困生(0:不是 1:是)
	 */
	private Boolean poorFlag;

	/**
	 * 抚养人
	 */
	private String dependant;

	/**
	 * 学费支付方式(1:现金 2:银行卡 3:微信 4:支付宝)
	 */
	private Integer payType;

	/**
	 * 逻辑删除标记字段(1: 正常 -1: 已删除)。
	 */
	@TableLogic
	private Integer deletedFlag;

	@Mapper
	public interface SysStudentModelMapper extends BaseModelMapper<SysStudentVo, SysStudent> {
	}

	public static final SysStudentModelMapper INSTANCE = Mappers.getMapper(SysStudentModelMapper.class);
}
