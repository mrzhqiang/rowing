package com.github.mrzhqiang.rowing.api.system.monitor;

import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 秒表监视器。
 * <p>
 * 利用 {@link com.google.common.base.Stopwatch 谷歌秒表} 进行耗时统计分析。
 */
@Slf4j
@Aspect
@Component
@Order(2)
public class StopwatchMonitor {

    private static final String BEFORE_TEMPLATE = "{}-{} 秒表开始计时";
    private static final String AFTER_TEMPLATE = "{}-{} 秒表结束计时！耗时：{}";

    @Pointcut("@within(org.springframework.stereotype.Service) && execution(public * *())")
    public void businessService() {
        // 切中所有使用 @Service 标记的类中的所有 public 方法
    }

    @Around("businessService()")
    public Object handleLog(ProceedingJoinPoint point) throws Throwable {
        String className = point.getTarget().getClass().getSimpleName();
        String methodName = point.getSignature().getName();
        log.info(BEFORE_TEMPLATE, className, methodName);
        Stopwatch started = Stopwatch.createStarted();
        Object proceed = point.proceed();
        log.info(AFTER_TEMPLATE, className, methodName, started.stop());
        return proceed;
    }
}
