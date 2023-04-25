package com.course.app.common.ext.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * common-ext通用扩展模块的自动配置引导类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@EnableConfigurationProperties({CommonExtProperties.class})
public class CommonExtAutoConfig {
}
