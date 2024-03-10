package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.util.Authentications;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * 作为系统用户运行注解的切面。
 * <p>
 * 利用 AOP 进行系统用户的替代认证，等指定方法执行完，再替换为之前的认证。
 */
@Slf4j
@Aspect
@Component
@Order(3)
public class RunAsSystemAspect {

    /**
     * 系统认证信息。
     * <p>
     * 创建时使用随机密码，避免被猜到系统用户。
     * <p>
     * 注意：系统认证仅用于内部调用需要认证的方法时使用，不能作为正常用户进行认证。
     */
    private final Authentication system = UsernamePasswordAuthenticationToken.authenticated(
            Accounts.SYSTEM_USERNAME,
            UUID.randomUUID().toString(),
            AuthorityUtils.createAuthorityList(AccountType.ADMIN.getAuthority()));

    @Pointcut("@annotation(com.github.mrzhqiang.rowing.account.RunAsSystem)")
    public void runAsSystemPointcut() {
        // 切中所有标记 @RunAsSystem 注解的方法
    }

    @Around("runAsSystemPointcut()")
    public Object runAsSystemAround(ProceedingJoinPoint point) throws Throwable {
        SecurityContext oldContext = SecurityContextHolder.getContext();
        if (Optional.ofNullable(oldContext)
                .map(SecurityContext::getAuthentication)
                .flatMap(Authentications::findUsername)
                .filter(Accounts.SYSTEM_USERNAME::equals)
                .isPresent()) {
            if (log.isDebugEnabled()) {
                log.debug(I18nHolder.getAccessor().getMessage("RunAsSystemAspect.hasRunAsSystem",
                        "检测到当前认证信息是系统用户，无需再次模拟"));
            }
            // 已经是 system 认证，那就直接执行，不需要替换
            return point.proceed();
        }

        // 替换当前安全上下文为 system 认证
        // 这里的代码参考了 RunAsManager#buildRunAs 在 AbstractSecurityInterceptor 中执行时的逻辑
        // 但 RunAsManager 是在已认证的情况下，为了调用更高权限的本地方法或远程调用方法采取的折中措施
        // 我们只需要实现在无认证情况下，支持系统调用相关方法，仅此而已
        SecurityContext systemContext = SecurityContextHolder.createEmptyContext();
        systemContext.setAuthentication(system);
        SecurityContextHolder.setContext(systemContext);
        if (log.isDebugEnabled()) {
            log.debug(I18nHolder.getAccessor().getMessage(
                    "RunAsSystemAspect.replace", new Object[]{oldContext},
                    Strings.lenientFormat("已将当前认证信息 %s 替换为系统用户", oldContext)));
        }

        Object result;
        try {
            // 执行目标方法
            result = point.proceed();
        } finally {
            SecurityContextHolder.setContext(oldContext);
            if (log.isDebugEnabled()) {
                log.debug(I18nHolder.getAccessor().getMessage(
                        "RunAsSystemAspect.restore", new Object[]{oldContext},
                        Strings.lenientFormat("已还原为之前的认证信息 %s", oldContext)));
            }
        }
        return result;
    }

}
