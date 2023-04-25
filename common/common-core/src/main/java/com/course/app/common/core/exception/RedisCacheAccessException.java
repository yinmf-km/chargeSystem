package com.course.app.common.core.exception;

/**
 * Redis缓存访问失败。比如：获取分布式数据锁超时、等待线程中断等。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public class RedisCacheAccessException  extends RuntimeException {

    /**
     * 构造函数。
     *
     * @param msg   错误信息。
     * @param cause 原始异常。
     */
    public RedisCacheAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
