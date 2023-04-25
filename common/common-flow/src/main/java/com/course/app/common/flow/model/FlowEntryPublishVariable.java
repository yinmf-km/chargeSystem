package com.course.app.common.flow.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * FlowEntryPublishVariable实体对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@TableName(value = "zz_flow_entry_publish_variable")
public class FlowEntryPublishVariable {

    /**
     * 主键Id。
     */
    @TableId(value = "variable_id")
    private Long variableId;

    /**
     * 流程Id。
     */
    @TableField(value = "entry_publish_id")
    private Long entryPublishId;

    /**
     * 变量名。
     */
    @TableField(value = "variable_name")
    private String variableName;

    /**
     * 显示名。
     */
    @TableField(value = "show_name")
    private String showName;

    /**
     * 变量类型。
     */
    @TableField(value = "variable_type")
    private Integer variableType;

    /**
     * 是否内置。
     */
    @TableField(value = "builtin")
    private Boolean builtin;

    /**
     * 绑定数据源Id。
     */
    @TableField(value = "bind_datasource_id")
    private Long bindDatasourceId;

    /**
     * 绑定数据源关联Id。
     */
    @TableField(value = "bind_relation_id")
    private Long bindRelationId;

    /**
     * 绑定字段Id。
     */
    @TableField(value = "bind_column_id")
    private Long bindColumnId;
}
