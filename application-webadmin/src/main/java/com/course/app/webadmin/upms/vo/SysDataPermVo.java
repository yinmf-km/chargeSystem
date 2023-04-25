package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.*;

/**
 * 数据权限VO。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("数据权限VO")
@Data
public class SysDataPermVo {

    /**
     * 数据权限Id。
     */
    @ApiModelProperty(value = "数据权限Id")
    private Long dataPermId;

    /**
     * 显示名称。
     */
    @ApiModelProperty(value = "显示名称")
    private String dataPermName;

    /**
     * 数据权限规则类型(0: 全部可见 1: 只看自己 2: 只看本部门 3: 本部门及子部门 4: 多部门及子部门 5: 自定义部门列表)。
     */
    @ApiModelProperty(value = "数据权限规则类型")
    private Integer ruleType;

    /**
     * 部门Id列表(逗号分隔)。
     */
    @ApiModelProperty(value = "部门Id列表")
    private String deptIdListString;

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
     * 数据权限与部门关联对象列表。
     */
    @ApiModelProperty(value = "数据权限与部门关联对象列表")
    private List<Map<String, Object>> dataPermDeptList;

    /**
     * 数据权限与菜单关联对象列表。
     */
    @ApiModelProperty(value = "数据权限与菜单关联对象列表")
    private List<Map<String, Object>> dataPermMenuList;
}