package com.course.app.common.core.annotation;

import java.lang.annotation.*;

/**
 * 主要用于标记通过租户Id进行过滤的字段。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantFilterColumn {

}
