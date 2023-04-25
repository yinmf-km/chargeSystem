package com.course.app.common.dict.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户全局系统字典Vo。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("租户全局系统字典Vo")
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantGlobalDictVo extends GlobalDictVo {

    /**
     * 是否为所有租户的通用字典。
     */
    @ApiModelProperty(value = "是否为所有租户的通用字典")
    private Boolean tenantCommon;

    /**
     * 租户的非公用字典的初始化字典数据。
     */
    @ApiModelProperty(value = "租户的非公用字典的初始化字典数据")
    private String initialData;
}
