package com.course.app.common.core.annotation;

import java.lang.annotation.*;

/**
 * 作为DisableDataFilterAspect的切点。
 * 该注解仅能标记在方法上，方法内所有的查询语句，均不会被Mybatis拦截器过滤数据。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DisableDataFilter {

}
