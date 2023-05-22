package com.course.app.webadmin.upms.dto;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * DormFeeFreeDetailDto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("DormFeeFreeDetailDto对象")
@Data
public class DormFeeFreeDetailDto {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id", required = true)
    @NotNull(message = "数据验证失败，主键Id不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 学生ID。
     */
    @ApiModelProperty(value = "学生ID", required = true)
    @NotNull(message = "数据验证失败，学生ID不能为空！")
    private Long studentId;

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
