package com.course.app.common.core.upload;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

/**
 * 业务对象根据上传下载存储类型，获取上传下载对象的工厂类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Component
public class UpDownloaderFactory {

    private final Map<UploadStoreTypeEnum, BaseUpDownloader> upDownloaderMap = new EnumMap<>(UploadStoreTypeEnum.class);

    /**
     * 根据存储类型获取上传下载对象。
     * @param storeType 存储类型。
     * @return 匹配的上传下载对象。
     */
    public BaseUpDownloader get(UploadStoreTypeEnum storeType) {
        BaseUpDownloader upDownloader = upDownloaderMap.get(storeType);
        if (upDownloader == null) {
            throw new UnsupportedOperationException(
                    "The storeType [" + storeType.name() + "] isn't supported, please add dependency jar first.");
        }
        return upDownloader;
    }

    /**
     * 注册上传下载对象到工厂。
     *
     * @param storeType    存储类型。
     * @param upDownloader 上传下载对象。
     */
    public void registerUpDownloader(UploadStoreTypeEnum storeType, BaseUpDownloader upDownloader) {
        if (storeType == null || upDownloader == null) {
            throw new IllegalArgumentException("The Argument can't be NULL.");
        }
        if (upDownloaderMap.containsKey(storeType)) {
            throw new UnsupportedOperationException(
                    "The storeType [" + storeType.name() + "] has been registered already.");
        }
        upDownloaderMap.put(storeType, upDownloader);
    }
}
