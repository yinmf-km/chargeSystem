package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 菜单与权限字关联实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@TableName(value = "sys_menu_perm_code")
public class SysMenuPermCode {

    /**
     * 关联菜单Id。
     */
    private Long menuId;

    /**
     * 关联权限字Id。
     */
    private Long permCodeId;
}
