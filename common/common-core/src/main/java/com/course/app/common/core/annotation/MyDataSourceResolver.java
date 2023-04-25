package com.course.app.common.core.annotation;

import com.course.app.common.core.util.DataSourceResolver;

import java.lang.annotation.*;

/**
 * 基于自定义解析规则的多数据源注解。主要用于标注Service的实现类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyDataSourceResolver {

    /**
     * 多数据源路由键解析接口的Class。
     * @return 多数据源路由键解析接口的Class。
     */
    Class<? extends DataSourceResolver> resolver();

    /**
     * DataSourceResolver.resovle方法的入参。
     * @return DataSourceResolver.resovle方法的入参。
     */
    String arg() default "";
}
