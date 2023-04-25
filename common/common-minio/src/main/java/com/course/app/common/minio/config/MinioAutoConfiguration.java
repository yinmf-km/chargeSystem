package com.course.app.common.minio.config;

import com.course.app.common.core.exception.MyRuntimeException;
import com.course.app.common.minio.wrapper.MinioTemplate;
import io.minio.MinioClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * common-minio模块的自动配置引导类。仅当配置项minio.enabled为true的时候加载。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@EnableConfigurationProperties(MinioProperties.class)
@ConditionalOnProperty(prefix = "minio", name = "enabled")
public class MinioAutoConfiguration {

    /**
     * 将minio原生的客户端类封装成bean对象，便于集成，同时也可以灵活使用客户端的所有功能。
     *
     * @param p 属性配置对象。
     * @return minio的原生客户端对象。
     */
    @Bean
    @ConditionalOnMissingBean
    public MinioClient minioClient(MinioProperties p) {
        try {
            MinioClient client = new MinioClient(p.getEndpoint(), p.getAccessKey(), p.getSecretKey());
            if (!client.bucketExists(p.getBucketName())) {
                client.makeBucket(p.getBucketName());
            }
            return client;
        } catch (Exception e) {
            throw new MyRuntimeException(e);
        }
    }

    /**
     * 封装的minio模板类。
     *
     * @param p 属性配置对象。
     * @param c minio的原生客户端bean对象。
     * @return minio模板的bean对象。
     */
    @Bean
    @ConditionalOnMissingBean
    public MinioTemplate minioTemplate(MinioProperties p, MinioClient c) {
        return new MinioTemplate(p, c);
    }
}