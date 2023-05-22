package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description H5学生信息VO
 * @author yinmf
 * @Title SysStudentVo.java
 * @Package com.course.app.webadmin.upms.vo
 * @date 2023年4月24日 下午11:43:06
 * @version V1.0
 */
@ApiModel("H5SysStudentVo视图对象")
@Data
@EqualsAndHashCode(callSuper = true)
public class H5SysStudentVo extends BaseVo {

    /**
     * 学号
     */
    @ApiModelProperty(value = "学号")
    private String studentNo;

    /**
     * 学生名称
     */
    @ApiModelProperty(value = "学生名称")
    private String studentName;

    /**
     * 性别(1:男 2:女)
     */
    @ApiModelProperty(value = "性别(1:男 2:女)")
    private Integer sex;

    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证号码")
    private String identityCard;

    /**
     * 毕业学校
     */
    @ApiModelProperty(value = "毕业学校")
    private String gradeSchool;

    /**
     * 学籍
     */
    @ApiModelProperty(value = "学籍")
    private String studentStatusDistName;

    /**
     * 班级
     */
    @ApiModelProperty(value = "班级名称")
    private String className;

    /**
     * 住宿信息
     */
    @ApiModelProperty(value = "住宿信息(楼栋+宿舍编号)")
    private String dormInfo;

}
