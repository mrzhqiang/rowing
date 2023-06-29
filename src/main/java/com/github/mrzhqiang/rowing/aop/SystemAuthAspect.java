package com.github.mrzhqiang.rowing.aop;

import com.github.mrzhqiang.rowing.domain.SystemAuthScope;
import com.github.mrzhqiang.rowing.util.Authentications;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 系统用户处理器。
 * <p>
 * 利用 AOP 进行系统用户的自认证处理，避免手动编码的繁琐性，但这会提升调试代码以及排查异常的难度。
 */
@Slf4j
@Aspect
@Component
@Order(3)
public class SystemAuthAspect {

    private final Authentication system;

    public SystemAuthAspect(SecurityProperties securityProperties) {
        this.system = Authentications.ofSystem(securityProperties);
    }

    @Pointcut("@annotation(com.github.mrzhqiang.rowing.aop.SystemAuth)")
    public void withSystemUserPoint() {
        // 切中所有标记 @WithSystemUser 注解的方法
    }

    @Around("withSystemUserPoint()")
    public Object systemUserHandle(ProceedingJoinPoint point) throws Throwable {
        // 检测注解的范围属性，以决定是否获取当前安全上下文
        Method targetMethod = ((MethodSignature) point.getSignature()).getMethod();
        SystemAuth annotation = AnnotationUtils.findAnnotation(targetMethod, SystemAuth.class);
        SystemAuthScope scope = Optional.ofNullable(annotation)
                .map(SystemAuth::scope)
                .orElse(SystemAuthScope.DEFAULT);

        SecurityContext currentContext = SecurityContextHolder.getContext();
        // 如果不是默认范围，或者是默认范围但不存在当前用户认证，那么允许替换
        if (!SystemAuthScope.DEFAULT.equals(scope) || currentContext == null) {
            // 替换当前安全上下文为 system 认证
            // 这里的逻辑参考了 RunAsManager#buildRunAs 在 AbstractSecurityInterceptor 中执行时的逻辑
            SecurityContext systemContext = SecurityContextHolder.createEmptyContext();
            systemContext.setAuthentication(system);
            SecurityContextHolder.setContext(systemContext);
        }
        // 执行目标方法
        Object result = point.proceed();

        // 根据注解的范围，即当前上下文是否保留，决定是否还原安全上下文
        if (SystemAuthScope.CURRENT.equals(scope)) {
            SecurityContextHolder.setContext(currentContext);
        } else {
            SecurityContextHolder.clearContext();
        }

        return result;
    }
}
