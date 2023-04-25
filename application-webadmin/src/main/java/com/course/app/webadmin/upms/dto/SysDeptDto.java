package com.course.app.webadmin.upms.dto;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * SysDeptDto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("SysDeptDto对象")
@Data
public class SysDeptDto {

    /**
     * 部门Id。
     */
    @ApiModelProperty(value = "部门Id", required = true)
    @NotNull(message = "数据验证失败，部门Id不能为空！", groups = {UpdateGroup.class})
    private Long deptId;

    /**
     * 部门名称。
     */
    @ApiModelProperty(value = "部门名称", required = true)
    @NotBlank(message = "数据验证失败，部门名称不能为空！")
    private String deptName;

    /**
     * 显示顺序。
     */
    @ApiModelProperty(value = "显示顺序", required = true)
    @NotNull(message = "数据验证失败，显示顺序不能为空！")
    private Integer showOrder;

    /**
     * 父部门Id。
     */
    @ApiModelProperty(value = "父部门Id")
    private Long parentId;
}
