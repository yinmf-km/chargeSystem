package com.course.app.webadmin.upms.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.webadmin.upms.vo.SchoolFeeFreeDetailVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 学费减免明细表
 * @author yinmf
 * @Title SchoolFeeFreeDetail.java
 * @Package com.course.app.webadmin.upms.model
 * @date 2023年4月27日 上午12:35:57
 * @version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "school_fee_free_detail")
public class SchoolFeeFreeDetail extends BaseModel {

	/**
	 * 主键Id
	 */
	private Long id;

	/**
	 * 学生ID
	 */
	private Long studentId;

	/**
	 * 审核状态(0:审核中 1:同意 2:不同意)
	 */
	private Integer processFlag;

	/**
	 * 逻辑删除标记字段(1: 正常 -1: 已删除)。
	 */
	@TableLogic
	private Integer deletedFlag;

	@Mapper
	public interface SchoolFeeFreeDetailModelMapper extends BaseModelMapper<SchoolFeeFreeDetailVo, SchoolFeeFreeDetail> {
	}

	public static final SchoolFeeFreeDetailModelMapper INSTANCE = Mappers.getMapper(SchoolFeeFreeDetailModelMapper.class);
}
