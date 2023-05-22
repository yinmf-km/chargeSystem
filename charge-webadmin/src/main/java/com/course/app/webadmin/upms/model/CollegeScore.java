package com.course.app.webadmin.upms.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.webadmin.upms.vo.CollegeScoreVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * CollegeScore实体对象。
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "college_score")
public class CollegeScore extends BaseModel {

	/**
	 * 主键Id。
	 */
	@TableId(value = "id")
	private Long id;
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
	 * 地址。
	 */
	private String address;
	/**
	 * 毕业学校。
	 */
	private String gradeSchool;
	/**
	 * 中考分数。
	 */
	private Integer mseScore;
	/**
	 * 学籍。
	 */
	private String studentStatusDistId;
	/**
	 * 审核状态(0:审核中 1:同意 2:不同意)。
	 */
	private Integer processFlag;
	/**
	 * 当前审批人员Id。
	 */
	private Long approveId;
	/**
	 * 审批时间。
	 */
	private Long approveTime;
	/**
	 * 逻辑删除标记字段(1: 正常 -1: 已删除)。
	 */
	@TableLogic
	private Integer deletedFlag;

	@Mapper
	public interface CollegeScoreModelMapper extends BaseModelMapper<CollegeScoreVo, CollegeScore> {
	}

	public static final CollegeScoreModelMapper INSTANCE = Mappers.getMapper(CollegeScoreModelMapper.class);
}
