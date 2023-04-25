package com.course.app.common.flow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流工单Dto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("工作流工单Dto对象")
@Data
public class FlowWorkOrderDto {

    /**
     * 工单编码。
     */
    @ApiModelProperty(value = "工单编码")
    private String workOrderCode;

    /**
     * 流程状态。参考FlowTaskStatus常量值对象。
     */
    @ApiModelProperty(value = "流程状态")
    private Integer flowStatus;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤起始值")
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "createTime 范围过滤结束值")
    private String createTimeEnd;
}
