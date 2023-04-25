package com.course.app.common.sequence.generator;

/**
 * 分布式Id生成器的统一接口。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public interface MyIdGenerator {

    /**
     * 获取数值型分布式Id。
     *
     * @return 生成后的Id。
     */
    long nextLongId();

    /**
     * 获取字符型分布式Id。
     *
     * @return 生成后的Id。
     */
    String nextStringId();
}
