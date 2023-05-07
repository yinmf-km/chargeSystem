package com.course.app.webadmin.upms.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.webadmin.upms.vo.SysDormVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 宿舍实体对象
 * @author yinmf
 * @Title SysDorm.java
 * @Package com.course.app.webadmin.upms.model
 * @date 2023年4月27日 上午12:11:07
 * @version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dorm")
public class SysDorm extends BaseModel {

	/**
	 * 主键Id
	 */
	@TableId(value = "dorm_id")
	private Long dormId;

	/**
	 * 楼栋号
	 */
	private String buildNum;

	/**
	 * 宿舍编号
	 */
	private String dormNum;

	/**
	 * 宿舍类型
	 */
	private String dormType;

	/**
	 * 床位数
	 */
	private Integer bedNum;

	/**
	 * 费用规格(元/学期)
	 */
	private Integer fee;

	@Mapper
	public interface SysDormModelMapper extends BaseModelMapper<SysDormVo, SysDorm> {
	}

	public static final SysDormModelMapper INSTANCE = Mappers.getMapper(SysDormModelMapper.class);
}
