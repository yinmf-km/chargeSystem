package com.course.app.webadmin.upms.model;

import java.sql.Date;

import org.apache.ibatis.annotations.Mapper;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.common.core.base.model.BaseModel;
import com.course.app.webadmin.upms.vo.SysStudentVo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 学生实体对象
 * @author yinmf
 * @Title SysStudent.java
 * @Package com.course.app.webadmin.upms.model
 * @date 2023年4月24日 下午11:31:01
 * @version V1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_student")
public class SysStudent extends BaseModel {

    /**
     * 主键ID
     */
    @TableId(value = "student_id")
    private Long studentId;
    /**
     * 学号
     */
    @TableField("student_no")
    private String studentNo;

    /**
     * 学生名称
     */
    @TableField("student_name")
    private String studentName;

    /**
     * 性别(1:男 2:女)
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 身份证号码
     */
    @TableField("identity_card")
    private String identityCard;

    /**
     * 家庭地址
     */
    @TableField("home_address")
    private String homeAddress;

    /**
     * 户籍地址
     */
    @TableField("domicile_address")
    private String domicileAddress;

    /**
     * 毕业学校
     */
    @TableField("grade_school")
    private String gradeSchool;

    /**
     * 中考分数
     */
    @TableField("mse_score")
    private Integer mseScore;

    /**
     * 生源地名称
     */

    @TableField("student_status_dist_nm")
    private String studentStatusDistNm;

    /**
     * 宿舍Id
     */
    @TableField("dorm_id")
    private Long dormId;

    /**
     * 班级ID
     */
    @TableField("class_id")
    private Long classId;

    /**
     * 是否贫困生(0:不是 1:是)
     */
    @TableField("poor_flag")
    private Boolean poorFlag;

    /**
     * 抚养人
     */
    @TableField("dependant")
    private String dependant;

    /**
     * 学生绑定手机号码
     */
    @TableField("phone_num")
    private String phoneNum;

    /**
     * 学费支付方式(1:现金 2:银行卡 3:微信 4:支付宝)
     */
    @TableField("pay_type")
    private Integer payType;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    @TableField("deleted_flag")
    private Integer deletedFlag;

    /**
     * 政治面貌
     */
    @TableField("political_outlook")
    private String politicalOutlook;

    /**
     * 学籍类型(1:统招 2:民办 3:借读)
     */
    @TableField("status_type")
    private Integer statusType;

    /**
     * 家长备注
     */
    @TableField("parent_notes")
    private String parentNotes;

    /**
     * 学籍状态(1:入学 2:休学 3:退学)
     */
    @TableField("student_status")
    private Integer studentStatus;

    /**
     * 入学时间
     */
    @TableField("enrol_time")
    private Date enrolTime;

    /**
     * 退学时间
     */
    @TableField("dropout_time")
    private Date dropoutTime;

    /**
     * 休学时间
     */
    @TableField("ost_time")
    private Date ostTime;

    /**
     * 注册来源(1:手机端 2:PC)
     */
    @TableField("register_type")
    private Integer registerType;

    @Mapper
    public interface SysStudentModelMapper extends BaseModelMapper<SysStudentVo, SysStudent> {}

    public static final SysStudentModelMapper INSTANCE =
        Mappers.getMapper(SysStudentModelMapper.class);
}
