package com.course.app.webadmin.upms.dto;

import com.course.app.common.core.validator.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 权限资源Dto。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("权限资源Dto")
@Data
public class SysPermDto {

    /**
     * 权限资源Id。
     */
    @ApiModelProperty(value = "权限资源Id", required = true)
    @NotNull(message = "权限Id不能为空！", groups = {UpdateGroup.class})
    private Long permId;

    /**
     * 权限资源名称。
     */
    @ApiModelProperty(value = "权限资源名称", required = true)
    @NotBlank(message = "权限资源名称不能为空！")
    private String permName;

    /**
     * shiro格式的权限字，如(upms:sysUser:add)。
     */
    @ApiModelProperty(value = "权限字")
    private String permCode;

    /**
     * 权限所在的权限模块Id。
     */
    @ApiModelProperty(value = "权限所在的权限模块Id")
    @NotNull(message = "权限模块Id不能为空！")
    private Long moduleId;

    /**
     * 关联的URL。
     */
    @ApiModelProperty(value = "关联的URL", required = true)
    @NotBlank(message = "权限关联的url不能为空！")
    private String url;

    /**
     * 权限在当前模块下的顺序，由小到大。
     */
    @ApiModelProperty(value = "显示顺序", required = true)
    @NotNull(message = "权限显示顺序不能为空！")
    private Integer showOrder;
}