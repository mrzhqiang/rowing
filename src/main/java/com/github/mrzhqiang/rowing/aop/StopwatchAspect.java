package com.github.mrzhqiang.rowing.aop;

import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.google.common.base.Stopwatch;
import com.google.common.base.Strings;
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
public class StopwatchAspect {

    @Pointcut("@within(org.springframework.stereotype.Service) && execution(public * *(..))")
    public void businessService() {
        // 切中所有使用 @Service 标记的类中的所有 public 方法
    }

    @Around("businessService()")
    public Object handleLog(ProceedingJoinPoint point) throws Throwable {
        String className = point.getTarget().getClass().getSimpleName();
        String methodName = point.getSignature().getName();
        log.info(I18nHolder.getAccessor().getMessage(
                "StopwatchAspect.handleLog.before", new Object[]{className, methodName},
                Strings.lenientFormat("%s-%s 开始计时", className, methodName)));
        Stopwatch started = Stopwatch.createStarted();
        Object proceed = point.proceed();
        Stopwatch stop = started.stop();
        log.info(I18nHolder.getAccessor().getMessage(
                "StopwatchAspect.handleLog.after", new Object[]{className, methodName, stop},
                Strings.lenientFormat("%s-%s 结束计时，耗时：%s", className, methodName, stop)));
        return proceed;
    }

}
