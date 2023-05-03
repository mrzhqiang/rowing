package com.github.mrzhqiang.rowing.util;

import com.github.mrzhqiang.rowing.account.CurrentUser;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 认证工具。
 */
public final class Authentications {
    private Authentications() {
        // no instances
    }

    /**
     * 未知用户的占位符。
     */
    public static final String UNKNOWN_USER_HOLDER = "(unknown-user)";
    /**
     * 未知主机的占位符。
     */
    public static final String UNKNOWN_HOST_HOLDER = "(unknown-host)";

    /**
     * 用来判断当前 Authentication 属于匿名用户还是记住用户。
     */
    private static final AuthenticationTrustResolver TRUST_RESOLVER =
            new AuthenticationTrustResolverImpl();

    /**
     * 当前安全上下文的认证信息。
     * <p>
     * 这个方法可以被 {@link CurrentUser} 替代，用于 Controller 层的方法参数。
     */
    public static Optional<Authentication> ofCurrent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication);
    }

    /**
     * 当前登录的认证信息。
     * <p>
     * 注意，如果没有 Authentication 实例，或者 Authentication 没有通过验证，或者 Authentication 是一个匿名实例，则返回不存在。
     *
     * @return 可选的实例，表示有可能返回不存在，但永远不会是 null 值。
     */
    public static Optional<Authentication> ofLogin() {
        return Authentications.ofCurrent()
                .filter(Authentication::isAuthenticated)
                .filter(it -> !TRUST_RESOLVER.isAnonymous(it));
    }

    /**
     * 从认证信息中获取用户名。
     *
     * @param authentication 认证信息。可能是登录用户，也可能是匿名用户。
     * @return 用户名字符串。
     */
    public static Optional<String> findUsername(Authentication authentication) {
        return Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .map(Authentications::attemptFindUsername);
    }

    private static String attemptFindUsername(Object it) {
        if (it == null) {
            return UNKNOWN_USER_HOLDER;
        }
        if (it instanceof UserDetails) {
            return ((UserDetails) it).getUsername();
        }
        return String.valueOf(it);
    }

    /**
     * 获取当前请求的用户名。
     */
    public static String currentUsername() {
        return Authentications.ofLogin()
                .flatMap(Authentications::findUsername)
                .orElse(UNKNOWN_USER_HOLDER);
    }

    /**
     * 获取当前请求的主机地址。
     */
    public static String currentHost() {
        return Authentications.ofCurrent()
                .map(Authentication::getDetails)
                .map(Authentications::attemptFindHost)
                .orElse(UNKNOWN_HOST_HOLDER);
    }

    private static String attemptFindHost(Object details) {
        if (details == null) {
            return UNKNOWN_HOST_HOLDER;
        }
        if (details instanceof WebAuthenticationDetails) {
            return ((WebAuthenticationDetails) details).getRemoteAddress();
        }
        return UNKNOWN_HOST_HOLDER;
    }

    /**
     * 通过自带安全属性生成 system 用户认证。
     *
     * @param properties Spring Security 内置属性配置。
     * @return 认证信息。
     */
    public static Authentication ofSystem(SecurityProperties properties) {
        SecurityProperties.User user = properties.getUser();
        String name = user.getName();
        String password = user.getPassword();
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(Authorizations::ofRole)
                .collect(Collectors.toList());
        return UsernamePasswordAuthenticationToken.authenticated(name, password, authorities);
    }
}
