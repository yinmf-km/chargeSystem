package com.course.app.webadmin.upms.model;

import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.webadmin.upms.vo.DormFeeFreeDetailVo;

import lombok.Data;

/**
 * DormFeeFreeDetail实体对象。
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@TableName(value = "dorm_fee_free_detail")
public class DormFeeFreeDetail {

	/**
	 * 主键Id。
	 */
	@TableId(value = "id")
	private Long id;
	/**
	 * 学生ID。
	 */
	private Long studentId;
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
	 * 创建者Id。
	 */
	private Long createUserId;
	/**
	 * 创建时间。
	 */
	private Date createTime;
	/**
	 * 逻辑删除标记字段(1: 正常 -1: 已删除)。
	 */
	@TableLogic
	private Integer deletedFlag;

	@Mapper
	public interface DormFeeFreeDetailModelMapper extends BaseModelMapper<DormFeeFreeDetailVo, DormFeeFreeDetail> {
	}

	public static final DormFeeFreeDetailModelMapper INSTANCE = Mappers.getMapper(DormFeeFreeDetailModelMapper.class);
}
