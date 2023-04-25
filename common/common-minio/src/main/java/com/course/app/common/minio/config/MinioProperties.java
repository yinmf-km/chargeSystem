package com.course.app.common.minio.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * common-minio模块的配置类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Data
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    /**
     * 访问入口地址。
     */
    private String endpoint;
    /**
     * 访问安全的key。
     */
    private String accessKey;
    /**
     * 访问安全的密钥。
     */
    private String secretKey;
    /**
     * 缺省桶名称。
     */
    private String bucketName;
}
