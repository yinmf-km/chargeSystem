package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * 数据权限与菜单关联实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@ToString(of = {"menuId"})
@TableName(value = "sys_data_perm_menu")
public class SysDataPermMenu {

    /**
     * 数据权限Id。
     */
    private Long dataPermId;

    /**
     * 关联菜单Id。
     */
    private Long menuId;
}
