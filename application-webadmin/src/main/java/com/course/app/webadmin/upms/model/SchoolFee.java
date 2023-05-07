package com.course.app.webadmin.upms.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.webadmin.upms.vo.SchoolFeeVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 学费信息表
 * @author yinmf
 * @Title SchoolFee.java
 * @Package com.course.app.webadmin.upms.model
 * @date 2023年4月27日 上午12:35:43
 * @version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "school_fee")
public class SchoolFee extends BaseModel {

	/**
	 * 主键Id
	 */
	private Long id;

	/**
	 * 生源地
	 */
	private String sourceAddress;

	/**
	 * 分数区间
	 */
	private String scoreRange;

	/**
	 * 规则描述
	 */
	private String ruleDesc;

	/**
	 * 费用
	 */
	private Integer fee;

	/**
	 * 逻辑删除标记字段(1: 正常 -1: 已删除)。
	 */
	@TableLogic
	private Integer deletedFlag;

	@Mapper
	public interface SchoolFeeModelMapper extends BaseModelMapper<SchoolFeeVo, SchoolFee> {
	}

	public static final SchoolFeeModelMapper INSTANCE = Mappers.getMapper(SchoolFeeModelMapper.class);
}
