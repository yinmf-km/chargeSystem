package com.course.app.webadmin.upms.dto;

import com.course.app.common.core.validator.AddGroup;
import com.course.app.common.core.validator.UpdateGroup;
import com.course.app.common.core.validator.ConstDictRef;
import com.course.app.webadmin.upms.model.constant.SysUserType;
import com.course.app.webadmin.upms.model.constant.SysUserStatus;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * SysUserDto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("SysUserDto对象")
@Data
public class SysUserDto {

    /**
     * 用户Id。
     */
    @ApiModelProperty(value = "用户Id", required = true)
    @NotNull(message = "数据验证失败，用户Id不能为空！", groups = {UpdateGroup.class})
    private Long userId;

    /**
     * 登录用户名。
     */
    @ApiModelProperty(value = "登录用户名", required = true)
    @NotBlank(message = "数据验证失败，登录用户名不能为空！")
    private String loginName;

    /**
     * 用户密码。
     */
    @ApiModelProperty(value = "用户密码", required = true)
    @NotBlank(message = "数据验证失败，用户密码不能为空！", groups = {AddGroup.class})
    private String password;

    /**
     * 用户部门Id。
     */
    @ApiModelProperty(value = "用户部门Id", required = true)
    @NotNull(message = "数据验证失败，用户部门Id不能为空！")
    private Long deptId;

    /**
     * 用户显示名称。
     */
    @ApiModelProperty(value = "用户显示名称", required = true)
    @NotBlank(message = "数据验证失败，用户显示名称不能为空！")
    private String showName;

    /**
     * 用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)。
     */
    @ApiModelProperty(value = "用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)", required = true)
    @NotNull(message = "数据验证失败，用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)不能为空！")
    @ConstDictRef(constDictClass = SysUserType.class, message = "数据验证失败，用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)为无效值！")
    private Integer userType;

    /**
     * 用户头像的Url。
     */
    @ApiModelProperty(value = "用户头像的Url")
    private String headImageUrl;

    /**
     * 用户状态(0: 正常 1: 锁定)。
     */
    @ApiModelProperty(value = "用户状态(0: 正常 1: 锁定)", required = true)
    @NotNull(message = "数据验证失败，用户状态(0: 正常 1: 锁定)不能为空！")
    @ConstDictRef(constDictClass = SysUserStatus.class, message = "数据验证失败，用户状态(0: 正常 1: 锁定)为无效值！")
    private Integer userStatus;

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

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤起始值(>=)")
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤结束值(<=)")
    private String createTimeEnd;
}
