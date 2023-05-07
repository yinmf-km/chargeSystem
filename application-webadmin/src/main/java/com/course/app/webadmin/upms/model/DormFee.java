package com.course.app.webadmin.upms.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.webadmin.upms.model.SysDept.SysDeptModelMapper;
import com.course.app.webadmin.upms.vo.DormFeeVo;
import com.course.app.webadmin.upms.vo.SysDeptVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 住宿费信息表
 * @author yinmf
 * @Title DormFee.java
 * @Package com.course.app.webadmin.upms.model
 * @date 2023年4月27日 上午12:35:33
 * @version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "dorm_fee")
public class DormFee extends BaseModel {

	/**
	 * 主键Id
	 */
	private Long id;

	/**
	 * 宿舍类型
	 */
	private String dormType;

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
	public interface DormFeeModelMapper extends BaseModelMapper<DormFeeVo, DormFee> {
	}

	public static final DormFeeModelMapper INSTANCE = Mappers.getMapper(DormFeeModelMapper.class);
}
