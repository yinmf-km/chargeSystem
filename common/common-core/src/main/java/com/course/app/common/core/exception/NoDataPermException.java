package com.course.app.common.core.exception;

/**
 * 没有数据访问权限的自定义异常。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public class NoDataPermException extends RuntimeException {

    /**
     * 构造函数。
     */
    public NoDataPermException() {

    }

    /**
     * 构造函数。
     *
     * @param msg 错误信息。
     */
    public NoDataPermException(String msg) {
        super(msg);
    }
}
