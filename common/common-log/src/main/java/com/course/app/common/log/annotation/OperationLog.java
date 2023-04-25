package com.course.app.common.log.annotation;

import com.course.app.common.log.model.constant.SysOperationLogType;

import java.lang.annotation.*;

/**
 * 操作日志记录注解。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    /**
     * 描述。
     */
    String description() default "";

    /**
     * 操作类型。
     */
    int type() default SysOperationLogType.OTHER;

    /**
     * 是否保存应答结果。
     * 对于类似导出和文件下载之类的接口，该参与应该设置为false。
     */
    boolean saveResponse() default true;
}
