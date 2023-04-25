package com.course.app.common.dict.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户全局系统字典项目实体类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "zz_tenant_global_dict_item")
public class TenantGlobalDictItem extends GlobalDictItem {

    /**
     * 租户Id。
     */
    @TableField(value = "tenant_id")
    private Long tenantId;
}
