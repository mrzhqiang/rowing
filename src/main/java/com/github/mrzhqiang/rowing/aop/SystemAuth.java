package com.github.mrzhqiang.rowing.aop;

import com.github.mrzhqiang.rowing.domain.SystemAuthScope;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 系统认证。
 * <p>
 * 用于标记目标方法，在方法执行之前，将自动认证为系统用户，等方法执行完毕，再根据认证范围处理认证信息。
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface SystemAuth {
    /**
     * 认证范围。
     * <p>
     * 默认的认证范围，将只在无用户认证的情况下，替换为系统认证，调用完成后自动清理认证。
     *
     * @return 认证范围。
     */
    SystemAuthScope scope() default SystemAuthScope.DEFAULT;
}
