package com.course.app.common.dict.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户全局系统字典实体类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "zz_tenant_global_dict")
public class TenantGlobalDict extends GlobalDict {

    /**
     * 是否为所有租户的通用字典。
     */
    @TableField(value = "tenant_common")
    private Boolean tenantCommon;

    /**
     * 租户的非公用字典的初始化字典数据。
     */
    @TableField(value = "initial_data")
    private String initialData;
}
