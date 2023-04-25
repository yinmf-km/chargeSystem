package com.course.app.common.log.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * common-log模块的自动配置引导类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@EnableConfigurationProperties({OperationLogProperties.class})
public class CommonLogAutoConfig {
}
