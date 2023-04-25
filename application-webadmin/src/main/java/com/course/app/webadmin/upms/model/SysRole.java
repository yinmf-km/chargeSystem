package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import com.course.app.common.core.annotation.RelationManyToMany;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.webadmin.upms.vo.SysRoleVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;

/**
 * 角色实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_role")
public class SysRole extends BaseModel {

    /**
     * 角色Id。
     */
    @TableId(value = "role_id")
    private Long roleId;

    /**
     * 角色名称。
     */
    private String roleName;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    @RelationManyToMany(
            relationMapperName = "sysRoleMenuMapper",
            relationMasterIdField = "roleId",
            relationModelClass = SysRoleMenu.class)
    @TableField(exist = false)
    private List<SysRoleMenu> sysRoleMenuList;

    @Mapper
    public interface SysRoleModelMapper extends BaseModelMapper<SysRoleVo, SysRole> {
        /**
         * 转换VO对象到实体对象。
         *
         * @param sysRoleVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "sysRoleMenuList", expression = "java(mapToBean(sysRoleVo.getSysRoleMenuList(), com.course.app.webadmin.upms.model.SysRoleMenu.class))")
        @Override
        SysRole toModel(SysRoleVo sysRoleVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param sysRole 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "sysRoleMenuList", expression = "java(beanToMap(sysRole.getSysRoleMenuList(), false))")
        @Override
        SysRoleVo fromModel(SysRole sysRole);
    }
    public static final SysRoleModelMapper INSTANCE = Mappers.getMapper(SysRole.SysRoleModelMapper.class);
}
