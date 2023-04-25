package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 角色菜单实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@TableName(value = "sys_role_menu")
public class SysRoleMenu {

    /**
     * 角色Id。
     */
    private Long roleId;

    /**
     * 菜单Id。
     */
    private Long menuId;
}
