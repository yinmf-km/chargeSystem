package com.course.app.common.flow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工作流通知消息Dto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("工作流通知消息Dto对象")
@Data
public class FlowMessageDto {

    /**
     * 消息类型。
     */
    @ApiModelProperty(value = "消息类型")
    private Integer messageType;

    /**
     * 工单Id。
     */
    @ApiModelProperty(value = "工单Id")
    private Long workOrderId;

    /**
     * 流程名称。
     */
    @ApiModelProperty(value = "流程名称")
    private String processDefinitionName;

    /**
     * 流程任务名称。
     */
    @ApiModelProperty(value = "流程任务名称")
    private String taskName;

    /**
     * 更新时间范围过滤起始值(>=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤起始值")
    private String updateTimeStart;

    /**
     * 更新时间范围过滤结束值(<=)。
     */
    @ApiModelProperty(value = "updateTime 范围过滤结束值")
    private String updateTimeEnd;
}
