package com.course.app.webadmin.upms.dto;

import com.course.app.common.core.validator.UpdateGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * SysDormDto对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("SysDormDto对象")
@Data
public class SysDormDto {

    /**
     * 主键Id。
     */
    @ApiModelProperty(value = "主键Id", required = true)
    @NotNull(message = "数据验证失败，主键Id不能为空！", groups = {UpdateGroup.class})
    private Long dormId;

    /**
     * 楼栋号。
     */
    @ApiModelProperty(value = "楼栋号", required = true)
    @NotBlank(message = "数据验证失败，楼栋号不能为空！")
    private String buildNum;

    /**
     * 宿舍编号。
     */
    @ApiModelProperty(value = "宿舍编号", required = true)
    @NotBlank(message = "数据验证失败，宿舍编号不能为空！")
    private String dormNum;

    /**
     * 宿舍类型。
     */
    @ApiModelProperty(value = "宿舍类型", required = true)
    @NotBlank(message = "数据验证失败，宿舍类型不能为空！")
    private String dormType;

    /**
     * 床位数。
     */
    @ApiModelProperty(value = "床位数", required = true)
    @NotNull(message = "数据验证失败，床位数不能为空！")
    private Integer bedNum;

    /**
     * 费用规格(元/学期)。
     */
    @ApiModelProperty(value = "费用规格(元/学期)", required = true)
    @NotNull(message = "数据验证失败，费用规格(元/学期)不能为空！")
    private Integer fee;

    /**
     * 是否满房(1: 是 -1: 否)。
     */
    @ApiModelProperty(value = "是否满房(1: 是 -1: 否)", required = true)
    @NotNull(message = "数据验证失败，是否满房(1: 是 -1: 否)不能为空！")
    private Integer isFull;
}
