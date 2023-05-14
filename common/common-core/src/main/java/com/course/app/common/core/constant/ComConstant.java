package com.course.app.common.core.constant;

/**
 * 
 * @Description 常量类定义
 * @author ymf
 * @Title  ComConstant.java
 * @Package com.course.app.common.core.constant
 * @date 2023年5月11日 上午12:04:30
 * @version V1.0
 */
public final class ComConstant {

    /**
     * 常用缓存有效时间
     */
    public interface CacheTime {

        /**
         * 一分钟
         */
        int EXPIR_ONE_MIN = 60;
        /**
         * 五分钟
         */
        int EXPIR_FIVE_MIN = 300;
        /**
         * 半小时
         */
        int EXPIR_HALF_HOUR = 1800;
        /**
         * 一小时
         */
        int EXPIR_HOUR = 3600;
        /**
         * 一天
         */
        int EXPIR_DAY = 86400;
        /**
         * 一周
         */
        int EXPIR_WEEK = 604800;
        /**
         * 一月，按30天计算
         */
        int EXPIR_MONTH = 2592000;
    }

    /**
     * 常用缓存key定义
     */
    public interface CacheKey {

        /**
         * 缓存key开头公共部分
         */
        String COMMON_KEY = "QYCHARGESYS:";
        /**
         * 后端管理平台验证码
         */
        String RANDOM_CODE_PLAT_KEY = "QYCHARGESYS:RANDOM_CODE:PLAT:%s";
        /**
         * 移动端验证码
         */
        String RANDOM_CODE_H5_KEY = "QYCHARGESYS:RANDOM_CODE:H5:%s";
    }

}
