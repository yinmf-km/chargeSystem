package com.course.app.common.flow.model;

import com.baomidou.mybatisplus.annotation.*;
import com.course.app.common.core.annotation.DeptFilterColumn;
import com.course.app.common.core.annotation.UserFilterColumn;
import com.course.app.common.core.annotation.RelationConstDict;
import com.course.app.common.core.base.mapper.BaseModelMapper;
import com.course.app.common.flow.constant.FlowTaskStatus;
import com.course.app.common.flow.vo.FlowWorkOrderVo;
import lombok.Data;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.Map;

/**
 * 工作流工单实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@TableName(value = "zz_flow_work_order")
public class FlowWorkOrder {

    /**
     * 主键Id。
     */
    @TableId(value = "work_order_id")
    private Long workOrderId;

    /**
     * 工单编码字段。
     */
    @TableField(value = "work_order_code")
    private String workOrderCode;

    /**
     * 流程定义标识。
     */
    @TableField(value = "process_definition_key")
    private String processDefinitionKey;

    /**
     * 流程名称。
     */
    @TableField(value = "process_definition_name")
    private String processDefinitionName;

    /**
     * 流程引擎的定义Id。
     */
    @TableField(value = "process_definition_id")
    private String processDefinitionId;

    /**
     * 流程实例Id。
     */
    @TableField(value = "process_instance_id")
    private String processInstanceId;

    /**
     * 在线表单的主表Id。
     */
    @TableField(value = "online_table_id")
    private Long onlineTableId;

    /**
     * 静态表单所使用的数据表名。
     */
    @TableField(value = "table_name")
    private String tableName;

    /**
     * 业务主键值。
     */
    @TableField(value = "business_key")
    private String businessKey;

    /**
     * 最近的审批状态。
     */
    @TableField(value = "latest_approval_status")
    private Integer latestApprovalStatus;

    /**
     * 流程状态。参考FlowTaskStatus常量值对象。
     */
    @TableField(value = "flow_status")
    private Integer flowStatus;

    /**
     * 提交用户登录名称。
     */
    @TableField(value = "submit_username")
    private String submitUsername;

    /**
     * 提交用户所在部门Id。
     */
    @DeptFilterColumn
    @TableField(value = "dept_id")
    private Long deptId;

    /**
     * 更新时间。
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 更新者Id。
     */
    @TableField(value = "update_user_id")
    private Long updateUserId;

    /**
     * 创建时间。
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 创建者Id。
     */
    @UserFilterColumn
    @TableField(value = "create_user_id")
    private Long createUserId;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @TableLogic
    @TableField(value = "deleted_flag")
    private Integer deletedFlag;

    /**
     * createTime 范围过滤起始值(>=)。
     */
    @TableField(exist = false)
    private String createTimeStart;

    /**
     * createTime 范围过滤结束值(<=)。
     */
    @TableField(exist = false)
    private String createTimeEnd;

    @RelationConstDict(
            masterIdField = "flowStatus",
            constantDictClass = FlowTaskStatus.class)
    @TableField(exist = false)
    private Map<String, Object> flowStatusDictMap;

    @Mapper
    public interface FlowWorkOrderModelMapper extends BaseModelMapper<FlowWorkOrderVo, FlowWorkOrder> {
    }
    public static final FlowWorkOrderModelMapper INSTANCE = Mappers.getMapper(FlowWorkOrderModelMapper.class);
}
