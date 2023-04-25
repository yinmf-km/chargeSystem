package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 用户角色实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@TableName(value = "sys_user_role")
public class SysUserRole {

    /**
     * 用户Id。
     */
    private Long userId;

    /**
     * 角色Id。
     */
    private Long roleId;
}
