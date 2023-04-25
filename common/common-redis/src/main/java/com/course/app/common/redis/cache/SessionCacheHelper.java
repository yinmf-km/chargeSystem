package com.course.app.common.redis.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrFormatter;
import com.course.app.common.core.object.TokenData;
import com.course.app.common.core.exception.MyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Session数据缓存辅助类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@SuppressWarnings("unchecked")
@Component
public class SessionCacheHelper {

    @Autowired
    private CacheManager cacheManager;

    /**
     * 缓存当前session内，上传过的文件名。
     *
     * @param filename 通常是本地存储的文件名，而不是上传时的原始文件名。
     */
    public void putSessionUploadFile(String filename) {
        if (filename != null) {
            Set<String> sessionUploadFileSet = null;
            Cache cache = cacheManager.getCache(RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name());
            if (cache == null) {
                String msg = StrFormatter.format("No redisson cache [{}].",
                        RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name());
                throw new MyRuntimeException(msg);
            }
            Cache.ValueWrapper valueWrapper = cache.get(TokenData.takeFromRequest().getSessionId());
            if (valueWrapper != null) {
                sessionUploadFileSet = (Set<String>) valueWrapper.get();
            }
            if (sessionUploadFileSet == null) {
                sessionUploadFileSet = new HashSet<>();
            }
            sessionUploadFileSet.add(filename);
            cache.put(TokenData.takeFromRequest().getSessionId(), sessionUploadFileSet);
        }
    }

    /**
     * 缓存当前Session可以下载的文件集合。
     *
     * @param filenameSet 后台服务本地存储的文件名，而不是上传时的原始文件名。
     */
    public void putSessionDownloadableFileNameSet(Set<String> filenameSet) {
        if (CollUtil.isEmpty(filenameSet)) {
            return;
        }
        Set<String> sessionUploadFileSet = null;
        Cache cache = cacheManager.getCache(RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name());
        if (cache == null) {
            throw new MyRuntimeException(StrFormatter.format("No redisson cache [{}]!",
                    RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name()));
        }
        Cache.ValueWrapper valueWrapper = cache.get(TokenData.takeFromRequest().getSessionId());
        if (valueWrapper != null) {
            sessionUploadFileSet = (Set<String>) valueWrapper.get();
        }
        if (sessionUploadFileSet == null) {
            sessionUploadFileSet = new HashSet<>();
        }
        sessionUploadFileSet.addAll(filenameSet);
        cache.put(TokenData.takeFromRequest().getSessionId(), sessionUploadFileSet);
    }

    /**
     * 判断参数中的文件名，是否有当前session上传。
     *
     * @param filename 通常是本地存储的文件名，而不是上传时的原始文件名。
     * @return true表示该文件是由当前session上传并存储在本地的，否则false。
     */
    public boolean existSessionUploadFile(String filename) {
        if (filename == null) {
            return false;
        }
        Cache cache = cacheManager.getCache(RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name());
        if (cache == null) {
            String msg = StrFormatter.format("No redisson cache [{}]!",
                    RedissonCacheConfig.CacheEnum.UPLOAD_FILENAME_CACHE.name());
            throw new MyRuntimeException(msg);
        }
        Cache.ValueWrapper valueWrapper = cache.get(TokenData.takeFromRequest().getSessionId());
        if (valueWrapper == null) {
            return false;
        }
        Object cachedData = valueWrapper.get();
        if (cachedData == null) {
            return false;
        }
        return ((Set<String>) cachedData).contains(filename);
    }

    /**
     * 清除当前session的所有缓存数据。
     *
     * @param sessionId 当前会话的SessionId。
     */
    public void removeAllSessionCache(String sessionId) {
        for (RedissonCacheConfig.CacheEnum c : RedissonCacheConfig.CacheEnum.values()) {
            Cache cache = cacheManager.getCache(c.name());
            if (cache != null) {
                cache.evict(sessionId);
            }
        }
    }
}
