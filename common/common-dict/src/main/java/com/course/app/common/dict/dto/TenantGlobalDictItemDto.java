package com.course.app.common.dict.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 租户全局系统字典项目Dto。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@ApiModel("租户全局系统字典项目Dto")
@EqualsAndHashCode(callSuper = true)
@Data
public class TenantGlobalDictItemDto extends GlobalDictItemDto {

}
