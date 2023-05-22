package com.course.app.webadmin.upms.dto;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * CollegeScoreDto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("CollegeScoreDto对象")
@Data
public class CollegeScoreDto {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id", required = true)
    @NotNull(message = "数据验证失败，主键Id不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 学生名称。
     */
    @ApiModelProperty(value = "学生名称", required = true)
    @NotBlank(message = "数据验证失败，学生名称不能为空！")
    private String studentName;

    /**
     * 性别(1:男 2:女)。
     */
    @ApiModelProperty(value = "性别(1:男 2:女)", required = true)
    @NotNull(message = "数据验证失败，性别(1:男 2:女)不能为空！")
    private Integer sex;

    /**
     * 身份证号码。
     */
    @ApiModelProperty(value = "身份证号码", required = true)
    @NotBlank(message = "数据验证失败，身份证号码不能为空！")
    private String identityCard;

    /**
     * 地址。
     */
    @ApiModelProperty(value = "地址", required = true)
    @NotBlank(message = "数据验证失败，地址不能为空！")
    private String address;

    /**
     * 毕业学校。
     */
    @ApiModelProperty(value = "毕业学校", required = true)
    @NotBlank(message = "数据验证失败，毕业学校不能为空！")
    private String gradeSchool;

    /**
     * 中考分数。
     */
    @ApiModelProperty(value = "中考分数")
    private Integer mseScore;

    /**
     * 学籍。
     */
    @ApiModelProperty(value = "学籍")
    private String studentStatusDistId;

    /**
     * 审核状态(0:审核中 1:同意 2:不同意)。
     */
    @ApiModelProperty(value = "审核状态(0:审核中 1:同意 2:不同意)", required = true)
    @NotNull(message = "数据验证失败，审核状态(0:审核中 1:同意 2:不同意)不能为空！")
    private Integer processFlag;

    /**
     * 当前审批人员Id。
     */
    @ApiModelProperty(value = "当前审批人员Id", required = true)
    @NotNull(message = "数据验证失败，当前审批人员Id不能为空！")
    private Long approveId;

    /**
     * 审批时间。
     */
    @ApiModelProperty(value = "审批时间", required = true)
    @NotNull(message = "数据验证失败，审批时间不能为空！")
    private Long approveTime;
}
