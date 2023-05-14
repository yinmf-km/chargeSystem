package com.course.app.webadmin.upms.model;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.webadmin.upms.vo.SysQuestVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 系统问题表
 * @author yinmf
 * @Title SysQuest.java
 * @Package com.course.app.webadmin.upms.model
 * @date 2023年4月27日 上午12:35:33
 * @version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_quest")
public class SysQuest extends BaseModel {

	/**
	 * 主键Id
	 */
	private Long id;
	/**
	 * 问题名称
	 */
	private String questName;
	/**
	 * 
	 */
	private String questAnswer;
	/**
	 * 逻辑删除标记字段(1: 正常 -1: 已删除)。
	 */
	@TableLogic
	private Integer deletedFlag;

	@Mapper
	public interface SysQuestModelMapper extends BaseModelMapper<SysQuestVo, SysQuest> {
	}

	public static final SysQuestModelMapper INSTANCE = Mappers.getMapper(SysQuestModelMapper.class);
}
