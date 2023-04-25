package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.*;

/**
 * 角色VO。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("角色VO")
@Data
public class SysRoleVo {

    /**
     * 角色Id。
     */
    @ApiModelProperty(value = "角色Id")
    private Long roleId;

    /**
     * 角色名称。
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * 创建者Id。
     */
    @ApiModelProperty(value = "创建者Id")
    private Long createUserId;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新者Id。
     */
    @ApiModelProperty(value = "更新者Id")
    private Long updateUserId;

    /**
     * 更新时间。
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 角色与菜单关联对象列表。
     */
    @ApiModelProperty(value = "角色与菜单关联对象列表")
    private List<Map<String, Object>> sysRoleMenuList;
}
