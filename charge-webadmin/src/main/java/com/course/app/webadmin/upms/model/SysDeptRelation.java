package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 部门关联实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_dept_relation")
public class SysDeptRelation {

    /**
     * 上级部门Id。
     */
    private Long parentDeptId;

    /**
     * 部门Id。
     */
    private Long deptId;
}
