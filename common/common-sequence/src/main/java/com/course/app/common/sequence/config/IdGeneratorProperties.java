package com.course.app.common.sequence.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * common-sequence模块的配置类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@ConfigurationProperties(prefix = "sequence")
public class IdGeneratorProperties {

    /**
     * 基础版生成器所需的WorkNode参数值。仅当advanceIdGenerator为false时生效。
     */
    private Integer snowflakeWorkNode = 1;
}
