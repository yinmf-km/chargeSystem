package com.course.app.common.flow.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.course.app.common.core.validator.UpdateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 流程分类的Dto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("流程分类的Dto对象")
@Data
public class FlowCategoryDto {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id")
    @NotNull(message = "数据验证失败，主键Id不能为空！", groups = {UpdateGroup.class})
    private Long categoryId;

    /**
     * 显示名称。
     */
    @ApiModelProperty(value = "显示名称")
    @NotBlank(message = "数据验证失败，显示名称不能为空！")
    private String name;

    /**
     * 分类编码。
     */
    @ApiModelProperty(value = "分类编码")
    @NotBlank(message = "数据验证失败，分类编码不能为空！")
    private String code;

    /**
     * 实现顺序。
     */
    @ApiModelProperty(value = "实现顺序")
    @NotNull(message = "数据验证失败，实现顺序不能为空！")
    private Integer showOrder;
}
