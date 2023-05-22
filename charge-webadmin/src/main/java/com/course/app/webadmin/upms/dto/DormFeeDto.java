package com.course.app.webadmin.upms.dto;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * DormFeeDto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("DormFeeDto对象")
@Data
public class DormFeeDto {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id", required = true)
    @NotNull(message = "数据验证失败，主键Id不能为空！", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 宿舍类型。
     */
    @ApiModelProperty(value = "宿舍类型", required = true)
    @NotBlank(message = "数据验证失败，宿舍类型不能为空！")
    private String dormType;

    /**
     * 费用。
     */
    @ApiModelProperty(value = "费用", required = true)
    @NotNull(message = "数据验证失败，费用不能为空！")
    private Integer fee;
}
