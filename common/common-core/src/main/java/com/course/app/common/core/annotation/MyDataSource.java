package com.course.app.common.core.annotation;

import java.lang.annotation.*;

/**
 * 主要用于标记Service所依赖的数据源类型。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyDataSource {

    /**
     * 标注的数据源类型
     * @return 当前标注的数据源类型。
     */
    int value();
}
