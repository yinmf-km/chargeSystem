package com.course.app.common.flow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 工作流工单VO对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("工作流工单Vo对象")
@Data
public class FlowWorkOrderVo {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id")
    private Long workOrderId;

    /**
     * 工单编码字段。
     */
    @ApiModelProperty(value = "工单编码字段")
    private String workOrderCode;

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
     * 流程引擎的定义Id。
     */
    @ApiModelProperty(value = "流程引擎的定义Id")
    private String processDefinitionId;

    /**
     * 流程实例Id。
     */
    @ApiModelProperty(value = "流程实例Id")
    private String processInstanceId;

    /**
     * 在线表单的主表Id。
     */
    @ApiModelProperty(value = "在线表单的主表Id")
    private Long onlineTableId;

    /**
     * 业务主键值。
     */
    @ApiModelProperty(value = "业务主键值")
    private String businessKey;

    /**
     * 最近的审批状态。
     */
    @ApiModelProperty(value = "最近的审批状态")
    private Integer latestApprovalStatus;

    /**
     * 流程状态。参考FlowTaskStatus常量值对象。
     */
    @ApiModelProperty(value = "流程状态")
    private Integer flowStatus;

    /**
     * 提交用户登录名称。
     */
    @ApiModelProperty(value = "提交用户登录名称")
    private String submitUsername;

    /**
     * 提交用户所在部门Id。
     */
    @ApiModelProperty(value = "提交用户所在部门Id")
    private Long deptId;

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
     * latestApprovalStatus 关联的字典数据。
     */
    @ApiModelProperty(value = "latestApprovalStatus 常量字典关联数据")
    private Map<String, Object> latestApprovalStatusDictMap;

    /**
     * flowStatus 常量字典关联数据。
     */
    @ApiModelProperty(value = "flowStatus 常量字典关联数据")
    private Map<String, Object> flowStatusDictMap;

    /**
     * 用户的显示名。
     */
    @ApiModelProperty(value = "用户的显示名")
    private String userShowName;

    /**
     * FlowEntryPublish对象中的同名字段。
     */
    @ApiModelProperty(value = "FlowEntryPublish对象中的同名字段")
    private String initTaskInfo;

    /**
     * 当前实例的运行时任务列表。
     * 正常情况下只有一个，在并行网关下可能存在多个。
     */
    @ApiModelProperty(value = "实例的运行时任务列表")
    private JSONArray runtimeTaskInfoList;

    /**
     * 业务主表数据。
     */
    @ApiModelProperty(value = "业务主表数据")
    private Map<String, Object> masterData;
}
