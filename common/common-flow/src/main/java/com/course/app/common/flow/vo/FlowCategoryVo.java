package com.course.app.common.flow.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 流程分类的Vo对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("流程分类的Vo对象")
@Data
public class FlowCategoryVo {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id")
    private Long categoryId;

    /**
     * 显示名称。
     */
    @ApiModelProperty(value = "显示名称")
    private String name;

    /**
     * 分类编码。
     */
    @ApiModelProperty(value = "分类编码")
    private String code;

    /**
     * 实现顺序。
     */
    @ApiModelProperty(value = "实现顺序")
    private Integer showOrder;

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
}
