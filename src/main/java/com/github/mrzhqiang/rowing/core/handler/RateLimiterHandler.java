package com.github.mrzhqiang.rowing.core.handler;

import com.github.mrzhqiang.rowing.core.session.SessionProperties;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import static org.springframework.web.context.request.RequestAttributes.REFERENCE_SESSION;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * 限流处理器。
 * <p>
 * 利用 {@link RateLimiter 谷歌限流器} 进行限流处理，即每 N 秒钟只允许执行一次。
 */
@SuppressWarnings("UnstableApiUsage")
@Slf4j
@Aspect
@Component
@Order(1)
public class RateLimiterHandler {

    private final SessionProperties properties;

    public RateLimiterHandler(SessionProperties properties) {
        this.properties = properties;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMappingPoint() {
        // aop point
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMappingPoint() {
        // aop point
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMappingPoint() {
        // aop point
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMappingPoint() {
        // aop point
    }

    @Around("getMappingPoint() || postMappingPoint() || putMappingPoint() || deleteMappingPoint()")
    public Object handle(ProceedingJoinPoint point) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpSession session = (HttpSession) requestAttributes.getAttribute(REFERENCE_SESSION, SCOPE_SESSION);
        // maybe not login
        if (session == null) {
            return point.proceed();
        }

        String rateLimiterKey = properties.getRateLimiter().getKeyName();
        RateLimiter rateLimiter = (RateLimiter) session.getAttribute(rateLimiterKey);
        if (rateLimiter == null) {
            double permits = properties.getRateLimiter().getPermits();
            rateLimiter = RateLimiter.create(permits);
            session.setAttribute(rateLimiterKey, rateLimiter);
        }
        boolean acquire = rateLimiter.tryAcquire();
        if (acquire) {
            return point.proceed();
        }
        return new ModelAndView("refresh");
    }
}