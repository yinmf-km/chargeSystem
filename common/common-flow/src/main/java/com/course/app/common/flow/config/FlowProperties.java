package com.course.app.common.flow.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 工作流的配置对象。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@ConfigurationProperties(prefix = "common-flow")
public class FlowProperties {

    /**
     * 工作落工单操作接口的URL前缀。
     */
    private String urlPrefix;
}
