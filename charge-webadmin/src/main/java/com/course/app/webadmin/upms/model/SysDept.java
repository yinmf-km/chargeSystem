package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.*;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.webadmin.upms.vo.SysDeptVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * SysDept实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dept")
public class SysDept extends BaseModel {

    /**
     * 部门Id。
     */
    @TableId(value = "dept_id")
    private Long deptId;

    /**
     * 部门名称。
     */
    private String deptName;

    /**
     * 显示顺序。
     */
    private Integer showOrder;

    /**
     * 父部门Id。
     */
    private Long parentId;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    @Mapper
    public interface SysDeptModelMapper extends BaseModelMapper<SysDeptVo, SysDept> {
    }
    public static final SysDeptModelMapper INSTANCE = Mappers.getMapper(SysDeptModelMapper.class);
}
