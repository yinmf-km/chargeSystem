package com.course.app.common.dict.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户全局系统字典Dto。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("租户全局系统字典Dto")
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantGlobalDictDto extends GlobalDictDto {

    /**
     * 是否为所有租户的通用字典。
     */
    @ApiModelProperty(value = "是否为所有租户的通用字典")
    private Boolean tenantCommon;
}
