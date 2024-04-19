package com.github.mrzhqiang.rowing.aop;

import com.github.mrzhqiang.rowing.config.SessionProperties;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;

import static org.springframework.web.context.request.RequestAttributes.REFERENCE_SESSION;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;

/**
 * 限流监视器。
 * <p>
 * 利用 {@link RateLimiter 谷歌限流器} 进行限流处理，即每个会话每秒钟只放行 N 个请求。
 * <p>
 * todo 前后端分离应该基于当前登录用户而不是会话进行处理。
 */
@SuppressWarnings("UnstableApiUsage")
@Slf4j
@Aspect
@Component
@Order(1)
@RequiredArgsConstructor
public class RateLimitAspect {

    private final SessionProperties properties;

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getPoint() {
        // 切中 GetMapping 注解标记的方法
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postPoint() {
        // 切中 PostMapping 注解标记的方法
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putPoint() {
        // 切中 PutMapping 注解标记的方法
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void patchPoint() {
        // 切中 PatchMapping 注解标记的方法
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deletePoint() {
        // 切中 DeleteMapping 注解标记的方法
    }

    @Around("getPoint() || postPoint() || putPoint() || patchPoint() || deletePoint()")
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
        // HTTP 状态码 429 表示请求过于频繁
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}
