package com.course.app.common.core.upload;

/**
 * 上传数据存储介质类型枚举。
 *
 * @author 云翼
 * @date 2023-02-21
 */
public enum UploadStoreTypeEnum {

    /**
     * 本地系统。
     */
    LOCAL_SYSTEM,
    /**
     * minio分布式存储。
     */
    MINIO_SYSTEM,
    /**
     * 阿里云OSS存储。
     */
    ALIYUN_OSS_SYTEM,
    /**
     * 腾讯云COS存储。
     */
    QCLOUD_COS_SYTEM
}
