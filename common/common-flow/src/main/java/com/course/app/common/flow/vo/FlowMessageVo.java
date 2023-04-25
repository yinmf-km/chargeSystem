package com.course.app.common.flow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 工作流通知消息Vo对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("工作流通知消息Vo对象")
@Data
public class FlowMessageVo {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id")
    private Long messageId;

    /**
     * 消息类型。
     */
    @ApiModelProperty(value = "消息类型")
    private Integer messageType;

    /**
     * 消息内容。
     */
    @ApiModelProperty(value = "消息内容")
    private String messageContent;

    /**
     * 催办次数。
     */
    @ApiModelProperty(value = "催办次数")
    private Integer remindCount;

    /**
     * 工单Id。
     */
    @ApiModelProperty(value = "工单Id")
    private Long workOrderId;

    /**
     * 流程定义Id。
     */
    @ApiModelProperty(value = "流程定义Id")
    private String processDefinitionId;

    /**
     * 流程定义标识。
     */
    @ApiModelProperty(value = "流程定义标识")
    private String processDefinitionKey;

    /**
     * 流程名称。
     */
    @ApiModelProperty(value = "流程名称")
    private String processDefinitionName;

    /**
     * 流程实例Id。
     */
    @ApiModelProperty(value = "流程实例Id")
    private String processInstanceId;

    /**
     * 流程实例发起者。
     */
    @ApiModelProperty(value = "流程实例发起者")
    private String processInstanceInitiator;

    /**
     * 流程任务Id。
     */
    @ApiModelProperty(value = "流程任务Id")
    private String taskId;

    /**
     * 流程任务定义标识。
     */
    @ApiModelProperty(value = "流程任务定义标识")
    private String taskDefinitionKey;

    /**
     * 流程任务名称。
     */
    @ApiModelProperty(value = "流程任务名称")
    private String taskName;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间")
    private Date taskStartTime;

    /**
     * 业务数据快照。
     */
    @ApiModelProperty(value = "业务数据快照")
    private String businessDataShot;

    /**
     * 更新时间。
     */
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    /**
     * 更新者Id。
     */
    @ApiModelProperty(value = "更新者Id")
    private Long updateUserId;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 创建者Id。
     */
    @ApiModelProperty(value = "创建者Id")
    private Long createUserId;

    /**
     * 创建者显示名。
     */
    @ApiModelProperty(value = "创建者显示名")
    private String createUsername;
}
