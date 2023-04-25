package com.course.app.common.flow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 流程变量Vo对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("流程变量Vo对象")
@Data
public class FlowEntryVariableVo {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id")
    private Long variableId;

    /**
     * 流程Id。
     */
    @ApiModelProperty(value = "流程Id")
    private Long entryId;

    /**
     * 变量名。
     */
    @ApiModelProperty(value = "变量名")
    private String variableName;

    /**
     * 显示名。
     */
    @ApiModelProperty(value = "显示名")
    private String showName;

    /**
     * 变量类型。
     */
    @ApiModelProperty(value = "变量类型")
    private Integer variableType;

    /**
     * 绑定数据源Id。
     */
    @ApiModelProperty(value = "绑定数据源Id")
    private Long bindDatasourceId;

    /**
     * 绑定数据源关联Id。
     */
    @ApiModelProperty(value = "绑定数据源关联Id")
    private Long bindRelationId;

    /**
     * 绑定字段Id。
     */
    @ApiModelProperty(value = "绑定字段Id")
    private Long bindColumnId;

    /**
     * 是否内置。
     */
    @ApiModelProperty(value = "是否内置")
    private Boolean builtin;

    /**
     * 创建时间。
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
}
