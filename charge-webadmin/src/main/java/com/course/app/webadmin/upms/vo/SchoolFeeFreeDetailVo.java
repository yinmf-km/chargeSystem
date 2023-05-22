package com.course.app.webadmin.upms.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * SchoolFeeFreeDetailVO视图对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("SchoolFeeFreeDetailVO视图对象")
@Data
public class SchoolFeeFreeDetailVo {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id")
    private Long id;

    /**
     * 学生ID。
     */
    @ApiModelProperty(value = "学生ID")
    private Long studentId;

    /**
     * 审核状态(0:审核中 1:同意 2:不同意)。
     */
    @ApiModelProperty(value = "审核状态(0:审核中 1:同意 2:不同意)")
    private Integer processFlag;

    /**
     * 当前审批人员Id。
     */
    @ApiModelProperty(value = "当前审批人员Id")
    private Long approveId;

    /**
     * 审批时间。
     */
    @ApiModelProperty(value = "审批时间")
    private Long approveTime;

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
}
