package com.course.app.webadmin.upms.dto;

import com.course.app.common.core.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * 角色Dto。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("角色Dto")
@Data
public class SysRoleDto {

    /**
     * 角色Id。
     */
    @ApiModelProperty(value = "角色Id", required = true)
    @NotNull(message = "角色Id不能为空！", groups = {UpdateGroup.class})
    private Long roleId;

    /**
     * 角色名称。
     */
    @ApiModelProperty(value = "角色名称", required = true)
    @NotBlank(message = "角色名称不能为空！")
    private String roleName;
}
