package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.MockScope;
import com.github.mrzhqiang.rowing.util.Authentications;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.annotation.AnnotationUtils;
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
public class SystemUserAspect {

    private final SecurityProperties securityProperties;

    public SystemUserAspect(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Pointcut("@annotation(com.github.mrzhqiang.rowing.account.SystemUserMock)")
    public void withSystemUserPoint() {
        // 切中所有标记 @WithSystemUser 注解的方法
    }

    @Around("withSystemUserPoint()")
    public Object systemUserHandle(ProceedingJoinPoint point) throws Throwable {
        // 检测注解的范围属性，以决定是否获取当前安全上下文
        Method targetMethod = ((MethodSignature) point.getSignature()).getMethod();
        SystemUserMock annotation = AnnotationUtils.findAnnotation(targetMethod, SystemUserMock.class);
        MockScope scope = Optional.ofNullable(annotation)
                .map(SystemUserMock::scope)
                .orElse(MockScope.GLOBAL);

        SecurityContext currentContext = null;
        if (MockScope.CURRENT.equals(scope)) {
            currentContext = SecurityContextHolder.getContext();
        }

        // 替换当前安全上下文为 system 认证
        Authentication system = Authentications.ofSystem(securityProperties);
        // 这里的逻辑参考了 RunAsManager#buildRunAs 在 AbstractSecurityInterceptor 中执行时的逻辑
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(system);
        SecurityContextHolder.setContext(context);
        // 执行目标方法
        Object result = point.proceed();

        // 根据注解的范围，即当前上下文是否保留，决定是否还原安全上下文
        if (currentContext != null) {
            SecurityContextHolder.setContext(currentContext);
        } else {
            SecurityContextHolder.clearContext();
        }

        return result;
    }
}
