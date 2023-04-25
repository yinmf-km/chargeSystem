package com.course.app.common.core.annotation;

import com.course.app.common.core.upload.UploadStoreTypeEnum;

import java.lang.annotation.*;

/**
 * 用于标记支持数据上传和下载的字段。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UploadFlagColumn {

    /**
     * 上传数据存储类型。
     *
     * @return 上传数据存储类型。
     */
    UploadStoreTypeEnum storeType();
}
