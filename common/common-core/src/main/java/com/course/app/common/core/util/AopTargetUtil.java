package com.course.app.common.core.util;

import cn.hutool.core.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * 获取JDK动态代理/CGLIB代理对象代理的目标对象的工具类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Slf4j
public class AopTargetUtil {

    /**
     * 获取参数对象代理的目标对象。
     *
     * @param proxy 代理对象
     * @return 代理的目标对象。
     */
    public static Object getTarget(Object proxy) {
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;
        }
        try {
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                return getJdkDynamicProxyTargetObject(proxy);
            } else {
                return getCglibProxyTargetObject(proxy);
            }
        } catch (Exception e) {
            log.error("Failed to call getJdkDynamicProxyTargetObject or getCglibProxyTargetObject", e);
            return null;
        }
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private AopTargetUtil() {
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        Object dynamicAdvisedInterceptor = ReflectUtil.getFieldValue(proxy, h);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        return ((AdvisedSupport) ReflectUtil.getFieldValue(dynamicAdvisedInterceptor, advised)).getTargetSource().getTarget();
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        AopProxy aopProxy = (AopProxy) ReflectUtil.getFieldValue(proxy, h);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        return ((AdvisedSupport) ReflectUtil.getFieldValue(aopProxy, advised)).getTargetSource().getTarget();
    }
}