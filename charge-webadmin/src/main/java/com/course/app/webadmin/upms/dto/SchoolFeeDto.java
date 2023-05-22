package com.course.app.webadmin.upms.dto;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * SchoolFeeDto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("SchoolFeeDto对象")
@Data
public class SchoolFeeDto {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id", required = true)
    @NotNull(message = "数据验证失败，主键Id不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 生源地。
     */
    @ApiModelProperty(value = "生源地", required = true)
    @NotBlank(message = "数据验证失败，生源地不能为空！")
    private String sourceAddress;

    /**
     * 分数区间。
     */
    @ApiModelProperty(value = "分数区间", required = true)
    @NotBlank(message = "数据验证失败，分数区间不能为空！")
    private String scoreRange;

    /**
     * 规则描述。
     */
    @ApiModelProperty(value = "规则描述", required = true)
    @NotBlank(message = "数据验证失败，规则描述不能为空！")
    private String ruleDesc;

    /**
     * 费用。
     */
    @ApiModelProperty(value = "费用", required = true)
    @NotNull(message = "数据验证失败，费用不能为空！")
    private Integer fee;
}
