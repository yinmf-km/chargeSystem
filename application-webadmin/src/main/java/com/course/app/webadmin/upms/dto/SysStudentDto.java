package com.course.app.webadmin.upms.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.course.app.common.core.validator.UpdateGroup;

import lombok.Data;

@Data
public class SysStudentDto {

    /**
     * 主键ID
     */
    @NotNull(message = "数据验证失败，主键Id不能为空！", groups = {UpdateGroup.class})
    private Long studentId;
    /**
     * 学号
     */
    @NotBlank(message = "数据验证失败，学号不能为空！")
    private String studentNo;

    /**
     * 学生名称
     */
    @NotBlank(message = "数据验证失败，学生名称不能为空！")
    private String studentName;

    /**
     * 性别(1:男 2:女)
     */
    @NotNull(message = "数据验证失败，性别(1:男 2:女)不能为空！")
    private Integer sex;

    /**
     * 身份证号码
     */
    @NotBlank(message = "数据验证失败，身份证号码不能为空！")
    private String identityCard;

    /**
     * 家庭地址
     */
    @NotBlank(message = "数据验证失败，家庭地址不能为空！")
    private String homeAddress;

    /**
     * 户籍地址
     */
    @NotBlank(message = "数据验证失败，户籍地址不能为空！")
    private String domicileAddress;

    /**
     * 毕业学校
     */
    @NotBlank(message = "数据验证失败，毕业学校不能为空！")
    private String gradeSchool;

    /**
     * 中考分数
     */
    @NotNull(message = "数据验证失败，中考分数不能为空！")
    private Integer mseScore;

    /**
     * 生源地名称
     */
    @NotBlank(message = "数据验证失败， 生源地不能为空！")
    private String studentStatusDistNm;

    /**
     * 宿舍Id
     */
    private Long dormId;

    /**
     * 班级ID
     */
    private Long classId;

    /**
     * 是否贫困生(0:不是 1:是)
     */
    private Boolean poorFlag;

    /**
     * 抚养人
     */
    @NotBlank(message = "数据验证失败， 抚养人不能为空！")
    private String dependant;

    /**
     * 学生绑定手机号码
     */
    @NotBlank(message = "数据验证失败， 手机号码不能为空！")
    private String phoneNum;

    /**
     * 学费支付方式(1:现金 2:银行卡 3:微信 4:支付宝)
     */
    private Integer payType;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    private Integer deletedFlag;

    /**
     * 政治面貌
     */
    private String politicalOutlook;

    /**
     * 学籍类型(1:统招 2:民办 3:借读)
     */
    private Integer statusType;

    /**
     * 家长备注
     */
    private String parentNotes;

    /**
     * 学籍状态(1:入学 2:休学 3:退学)
     */
    private Integer studentStatus;

    /**
     * 入学时间
     */
    private Date enrolTime;

    /**
     * 退学时间
     */
    private Date dropoutTime;

    /**
     * 休学时间
     */
    private Date ostTime;

    /**
     * 注册来源(1:手机端 2:PC)
     */
    private String registerType;
}
