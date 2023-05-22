package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * 数据权限与用户关联实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@TableName(value = "sys_data_perm_user")
public class SysDataPermUser {

    /**
     * 数据权限Id。
     */
    private Long dataPermId;

    /**
     * 用户Id。
     */
    private Long userId;
}
