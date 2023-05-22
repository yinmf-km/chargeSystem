package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 权限字与权限资源关联实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@TableName(value = "sys_perm_code_perm")
public class SysPermCodePerm {

    /**
     * 权限字Id。
     */
    private Long permCodeId;

    /**
     * 权限Id。
     */
    private Long permId;
}
