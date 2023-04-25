package com.course.app.common.redis.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Redis的常用工具方法。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Slf4j
@Component
public class CommonRedisUtil {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 生成基于时间的流水号方法。
     *
     * @param prefix      前缀字符串。
     * @param precisionTo 精确到的时间单元，目前仅仅支持 DAYS/HOURS/MINUTES/SECONDS。
     * @param middle      日期和流水号之间的字符串。
     * @param idWidth     计算出的流水号宽度，前面补充0。比如idWidth = 3, 输出值为 005/012/123。
     *                    需要注意的是，流水号值超出idWidth指定宽度，低位会被截取。
     * @return 基于时间的流水号方法。
     */
    public String generateTransId(String prefix, String precisionTo, String middle, int idWidth) {
        TimeUnit unit = TimeUnit.valueOf(precisionTo);
        return generateTransId(prefix, unit, middle, idWidth);
    }

    /**
     * 生成基于时间的流水号方法。
     *
     * @param prefix  前缀字符串。
     * @param unit    时间单元，目前仅仅支持 DAYS/HOURS/MINUTES/SECONDS。
     * @param middle  日期和流水号之间的字符串。
     * @param idWidth 计算出的流水号宽度，前面补充0。比如idWidth = 3, 输出值为 005/012/123。
     *                需要注意的是，流水号值超出idWidth指定宽度，低位会被截取。
     * @return 基于时间的流水号方法。
     */
    public String generateTransId(String prefix, TimeUnit unit, String middle, int idWidth) {
        String key = prefix;
        if (key == null) {
            key = "";
        }
        DateTime dateTime = new DateTime();
        switch (unit) {
            case DAYS:
                key = key + dateTime.toString("yyyyMMdd");
                break;
            case HOURS:
                key = key + dateTime.toString("yyyyMMddHH");
                break;
            case MINUTES:
                key = key + dateTime.toString("yyyyMMddHHmm");
                break;
            case SECONDS:
                key = key + dateTime.toString("yyyyMMddHHmmss");
                break;
            default:
                throw new UnsupportedOperationException("Only Support DAYS/HOURS/MINUTES/SECONDS");
        }
        if (middle != null) {
            key = key + middle;
        }
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        long value = atomicLong.incrementAndGet();
        if (value == 1L) {
            atomicLong.expire(1, unit);
        }
        return key + StrUtil.padPre(String.valueOf(value), idWidth, "0");
    }

    /**
     * 从缓存中获取数据。如果缓存中不存在则从执行指定的方法获取数据，并将得到的数据同步到缓存。
     *
     * @param key     缓存的键。
     * @param id      数据Id。
     * @param f       获取数据的方法。
     * @param clazz   数据对象类型。
     * @return 数据对象。
     */
    public <M> M getFromCache(String key, Serializable id, Function<Serializable, M> f, Class<M> clazz) {
        return this.getFromCache(key, id, f, clazz, null);
    }

    /**
     * 从缓存中获取数据。如果缓存中不存在则从执行指定的方法获取数据，并将得到的数据同步到缓存。
     *
     * @param key    缓存的键。
     * @param filter mybatis plus的过滤对象。
     * @param f      获取数据的方法。
     * @param clazz  数据对象类型。
     * @return 数据对象。
     */
    public <N> N getFromCache(
            String key, LambdaQueryWrapper<N> filter, Function<LambdaQueryWrapper<N>, N> f, Class<N> clazz) {
        N m;
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (!bucket.isExists()) {
            m = f.apply(filter);
            if (m != null) {
                bucket.set(JSON.toJSONString(m));
            }
        } else {
            m = JSON.parseObject(bucket.get(), clazz);
        }
        return m;
    }
        
    /**
     * 从缓存中获取数据。如果缓存中不存在则从执行指定的方法获取数据，并将得到的数据同步到缓存。
     *
     * @param key    缓存的键。
     * @param filter 过滤对象。
     * @param f      获取数据的方法。
     * @param clazz  数据对象类型。
     * @return 数据对象。
     */
    public <M, N> N getFromCache(String key, M filter, Function<M, N> f, Class<N> clazz) {
        N m;
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (!bucket.isExists()) {
            m = f.apply(filter);
            if (m != null) {
                bucket.set(JSON.toJSONString(m));
            }
        } else {
            m = JSON.parseObject(bucket.get(), clazz);
        }
        return m;
    }

    /**
     * 从缓存中获取数据。如果缓存中不存在则从执行指定的方法获取数据，并将得到的数据同步到缓存。
     *
     * @param key     缓存的键。
     * @param id      数据Id。
     * @param f       获取数据的方法。
     * @param clazz   数据对象类型。
     * @param seconds 过期秒数。
     * @return 数据对象。
     */
    public <M> M getFromCache(
            String key, Serializable id, Function<Serializable, M> f, Class<M> clazz, Integer seconds) {
        M m;
        RBucket<String> bucket = redissonClient.getBucket(key);
        if (!bucket.isExists()) {
            m = f.apply(id);
            if (m != null) {
                bucket.set(JSON.toJSONString(m));
                if (seconds != null) {
                    bucket.expire(seconds, TimeUnit.SECONDS);
                }
            }
        } else {
            m = JSON.parseObject(bucket.get(), clazz);
        }
        return m;
    }

    /**
     * 移除指定Key。
     *
     * @param key 键名。
     */
    public void evictFormCache(String key) {
        redissonClient.getBucket(key).delete();
    }
}
