package com.course.app.common.xxljob.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Job处理器的AOP，目前仅仅实现了将拦截后的异常记录到本地日志服务系统，记录后重新抛给xxl-job。
 *
 * @author 云翼
 * @date 2023-02-21
 */
@Aspect
@Component
@Order(1)
@Slf4j
public class JobHandlerAspect {

    @Pointcut("execution(public * com.course.app.*.handler..*(..))")
    public void handlerPointCut() {
        // 空注释，sonar要求的。
    }

    @Around("handlerPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        try {
            return point.proceed();
        } catch (Exception e) {
            log.error("JobHandler [" + point.getTarget().getClass().getSimpleName() + "] throws exception.", e);
            throw e;
        }
    }
}
