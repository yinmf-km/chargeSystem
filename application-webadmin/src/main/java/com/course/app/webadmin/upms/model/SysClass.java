package com.course.app.webadmin.upms.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.model.BaseModel;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 
 * @Description 班级实体对象
 * @author ymf
 * @Title  SysClass.java
 * @Package com.course.app.webadmin.upms.model
 * @date 2023年5月13日 下午10:16:59
 * @version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_class")
public class SysClass extends BaseModel {

    /**
     * 班级ID
     */
    @TableId(value = "class_id")
    private Long classId;
    /**
     * 班级名称
     */
    @TableField("class_name")
    private String className;
    /**
     * 班主任ID
     */
    @TableField("class_teacher_id")
    private Long classTeacherId;
    /**
     * 班主任名称
     */
    @TableField("class_teacher_name")
    private String classTeacherName;
}
