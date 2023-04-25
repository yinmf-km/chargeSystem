package com.course.app.common.core.annotation;

import java.lang.annotation.*;

/**
 * 标识Model和常量字典之间的关联关系。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RelationConstDict {

    /**
     * 当前对象的关联Id字段名称。
     *
     * @return 当前对象的关联Id字段名称。
     */
    String masterIdField();

    /**
     * 被关联的常量字典的Class对象。
     *
     * @return 关联的常量字典的Class对象。
     */
    Class<?> constantDictClass();
}
