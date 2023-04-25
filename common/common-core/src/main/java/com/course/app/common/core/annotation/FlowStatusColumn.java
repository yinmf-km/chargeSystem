package com.course.app.common.core.annotation;

import java.lang.annotation.*;

/**
 * 业务表中记录流程实例结束标记的字段。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FlowStatusColumn {

}
