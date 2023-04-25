package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import com.course.app.webadmin.upms.model.constant.SysUserType;
import com.course.app.webadmin.upms.model.constant.SysUserStatus;
import com.course.app.common.core.upload.UploadStoreTypeEnum;
import com.course.app.common.core.annotation.*;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.webadmin.upms.vo.SysUserVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Map;
import java.util.List;

/**
 * SysUser实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user")
public class SysUser extends BaseModel {

    /**
     * 用户Id。
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 登录用户名。
     */
    private String loginName;

    /**
     * 用户密码。
     */
    private String password;

    /**
     * 用户部门Id。
     */
    private Long deptId;

    /**
     * 显示名称。
     */
    private String showName;

    /**
     * 用户类型(0: 管理员 1: 普通操作员 2: 老师 3:助教)。
     */
    private Integer userType;

    /**
     * 用户头像的Url。
     */
    @UploadFlagColumn(storeType = UploadStoreTypeEnum.LOCAL_SYSTEM)
    private String headImageUrl;

    /**
     * 用户状态(0: 正常 1: 锁定)。
     */
    private Integer userStatus;

    /**
     * 用户邮箱。
     */
    private String email;

    /**
     * 用户手机。
     */
    private String mobile;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String createTimeEnd;

    /**
     * 多对多用户角色数据集合。
     */
    @RelationManyToMany(
            relationMapperName = "sysUserRoleMapper",
            relationMasterIdField = "userId",
            relationModelClass = SysUserRole.class)
    @TableField(exist = false)
    private List<SysUserRole> sysUserRoleList;

    /**
     * 多对多用户数据权限数据集合。
     */
    @RelationManyToMany(
            relationMapperName = "sysDataPermUserMapper",
            relationMasterIdField = "userId",
            relationModelClass = SysDataPermUser.class)
    @TableField(exist = false)
    private List<SysDataPermUser> sysDataPermUserList;

    @RelationDict(
            masterIdField = "deptId",
            slaveServiceName = "sysDeptService",
            slaveModelClass = SysDept.class,
            slaveIdField = "deptId",
            slaveNameField = "deptName")
    @TableField(exist = false)
    private Map<String, Object> deptIdDictMap;

    @RelationConstDict(
            masterIdField = "userType",
            constantDictClass = SysUserType.class)
    @TableField(exist = false)
    private Map<String, Object> userTypeDictMap;

    @RelationConstDict(
            masterIdField = "userStatus",
            constantDictClass = SysUserStatus.class)
    @TableField(exist = false)
    private Map<String, Object> userStatusDictMap;

    @Mapper
    public interface SysUserModelMapper extends BaseModelMapper<SysUserVo, SysUser> {
        /**
         * 转换Vo对象到实体对象。
         *
         * @param sysUserVo 域对象。
         * @return 实体对象。
         */
        @Mapping(target = "sysUserRoleList", expression = "java(mapToBean(sysUserVo.getSysUserRoleList(), com.course.app.webadmin.upms.model.SysUserRole.class))")
        @Mapping(target = "sysDataPermUserList", expression = "java(mapToBean(sysUserVo.getSysDataPermUserList(), com.course.app.webadmin.upms.model.SysDataPermUser.class))")
        @Override
        SysUser toModel(SysUserVo sysUserVo);
        /**
         * 转换实体对象到VO对象。
         *
         * @param sysUser 实体对象。
         * @return 域对象。
         */
        @Mapping(target = "sysUserRoleList", expression = "java(beanToMap(sysUser.getSysUserRoleList(), false))")
        @Mapping(target = "sysDataPermUserList", expression = "java(beanToMap(sysUser.getSysDataPermUserList(), false))")
        @Override
        SysUserVo fromModel(SysUser sysUser);
    }
    public static final SysUserModelMapper INSTANCE = Mappers.getMapper(SysUserModelMapper.class);
}
