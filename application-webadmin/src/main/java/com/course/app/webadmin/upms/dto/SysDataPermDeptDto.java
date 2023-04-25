package com.course.app.webadmin.upms.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据权限与部门关联Dto。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("数据权限与部门关联Dto")
@Data
public class SysDataPermDeptDto {

    /**
     * 数据权限Id。
     */
    @ApiModelProperty(value = "数据权限Id", required = true)
    private Long dataPermId;

    /**
     * 关联部门Id。
     */
    @ApiModelProperty(value = "关联部门Id", required = true)
    private Long deptId;
}