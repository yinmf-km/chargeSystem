package com.course.app.common.flow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 流程任务的用户信息。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("流程任务的用户信息")
@Data
public class FlowUserInfoVo {

    /**
     * 用户Id。
     */
    @ApiModelProperty(value = "用户Id")
    private Long userId;

    /**
     * 登录用户名。
     */
    @ApiModelProperty(value = "登录用户名")
    private String loginName;

    /**
     * 用户部门Id。
     */
    @ApiModelProperty(value = "用户部门Id")
    private Long deptId;

    /**
     * 用户显示名称。
     */
    @ApiModelProperty(value = "用户显示名称")
    private String showName;

    /**
     * 用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)。
     */
    @ApiModelProperty(value = "用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)")
    private Integer userType;

    /**
     * 用户头像的Url。
     */
    @ApiModelProperty(value = "用户头像的Url")
    private String headImageUrl;

    /**
     * 用户状态(0: 正常 1: 锁定)。
     */
    @ApiModelProperty(value = "用户状态(0: 正常 1: 锁定)")
    private Integer userStatus;

    /**
     * 最后审批时间。
     */
    @ApiModelProperty(value = "最后审批时间")
    private Date lastApprovalTime;

    /**
     * 用户邮箱。
     */
    @ApiModelProperty(value = "用户邮箱")
    private String email;

    /**
     * 用户手机。
     */
    @ApiModelProperty(value = "用户手机")
    private String mobile;
}
