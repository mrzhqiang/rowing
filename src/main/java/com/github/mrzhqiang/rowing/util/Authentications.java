package com.github.mrzhqiang.rowing.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Optional;

/**
 * 认证工具。
 */
@UtilityClass
public class Authentications {

    /**
     * 未知主机。
     * <p>
     * 注意：未知主机通常与系统用户同时出现，也可能在其他情况下出现，主要是避免空的主机数据被记录。
     */
    public static final String UNKNOWN_HOST = "(unknown)";

    /**
     * 用来判断当前 Authentication 属于游客还是用户。
     */
    private static final AuthenticationTrustResolver TRUST_RESOLVER = new AuthenticationTrustResolverImpl();

    /**
     * 已登录认证。
     * <p>
     * 注意，如果不存在 Authentication 实例，
     * 或者 Authentication 实例没有通过认证（即登录成功），
     * 或者 Authentication 实例是一个匿名认证，
     * 则返回不存在。
     *
     * @return 可选的实例，表示有可能返回不存在，但永远不会是 null 值。
     */
    public static Optional<Authentication> ofLogin() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated)
                .filter(it -> !TRUST_RESOLVER.isAnonymous(it));
    }

    /**
     * 获取认证用户名。
     *
     * @param authentication 认证信息。
     * @return 可选的用户名。
     */
    public static Optional<String> findUsername(Authentication authentication) {
        return Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .map(Authentications::attemptFindUsername);
    }

    private static String attemptFindUsername(Object it) {
        if (it == null) {
            return "";
        }
        if (it instanceof UserDetails) {
            return ((UserDetails) it).getUsername();
        }
        return String.valueOf(it);
    }

    /**
     * 获取当前请求的主机地址。
     * <p>
     * 注意：这个方法默认使用 unknown 主机名，如果存在主机信息，则使用主机地址。
     */
    public static String currentHost() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getDetails)
                .map(Authentications::attemptFindHost)
                .orElse(UNKNOWN_HOST);
    }

    private static String attemptFindHost(Object details) {
        if (details == null) {
            return UNKNOWN_HOST;
        }
        if (details instanceof WebAuthenticationDetails) {
            return ((WebAuthenticationDetails) details).getRemoteAddress();
        }
        return UNKNOWN_HOST;
    }

}
