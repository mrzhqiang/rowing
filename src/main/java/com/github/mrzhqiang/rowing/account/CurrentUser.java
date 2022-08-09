package com.github.mrzhqiang.rowing.account;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 当前用户。
 * <p>
 * 通过此注解，可以从当前会话中得到 {@link UserDetails} 实例。
 * <p>
 * 主要利用：{@link Authentication#getPrincipal()} 来获得实例对象。
 * <p>
 * 所标记的类必须是 {@link UserDetails} 接口，或其实现类，否则将得到 Null 值。
 *
 * @see AuthenticationPrincipalArgumentResolver
 */
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal
public @interface CurrentUser {
    // nothing here
}
