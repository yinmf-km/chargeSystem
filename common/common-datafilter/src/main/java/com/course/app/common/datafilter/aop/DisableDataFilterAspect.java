package com.course.app.common.datafilter.aop;

import com.course.app.common.core.object.GlobalThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 禁用Mybatis拦截器数据过滤的AOP处理类。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class DisableDataFilterAspect {

    /**
     * 所有标记了DisableDataFilter注解的方法。
     */
    @Pointcut("@annotation(com.course.app.common.core.annotation.DisableDataFilter)")
    public void disableDataFilterPointCut() {
        // 空注释，避免sonar警告
    }

    @Around("disableDataFilterPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        boolean dataFilterEnabled = GlobalThreadLocal.setDataFilter(false);
        try {
            return point.proceed();
        } finally {
            GlobalThreadLocal.setDataFilter(dataFilterEnabled);
        }
    }
}
