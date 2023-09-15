package com.github.mrzhqiang.rowing.util;

import com.github.mrzhqiang.rowing.domain.AccountType;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Optional;
import java.util.UUID;

/**
 * 认证工具。
 */
public final class Authentications {
    private Authentications() {
        // no instances
    }

    /**
     * 系统虚拟用户名。
     * <p>
     * 数据库审计需要一个系统虚拟用户。
     */
    public static final String SYSTEM_USERNAME = "system";
    /**
     * 超级管理员用户名。
     */
    public static final String ADMIN_USERNAME = "admin";
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
     * 系统认证。
     * <p>
     * 注意：系统认证仅用于内部调用需要认证的方法时使用，不能作为正常用户进行认证。
     *
     * @return 系统认证。
     */
    public static Authentication ofSystem() {
        return UsernamePasswordAuthenticationToken.authenticated(
                SYSTEM_USERNAME,
                UUID.randomUUID().toString(),
                AuthorityUtils.createAuthorityList(AccountType.ADMIN.name()));
    }

    /**
     * 获取认证用户名。
     *
     * @param authentication 认证信息。可能是用户，也可能是游客。
     * @return 可选的用户名。
     */
    public static Optional<String> findUsername(Authentication authentication) {
        return Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .map(Authentications::attemptFindUsername);
    }

    private static String attemptFindUsername(Object it) {
        if (it == null) {
            // 为审计而返回 system 用户
            return SYSTEM_USERNAME;
        }
        if (it instanceof UserDetails) {
            return ((UserDetails) it).getUsername();
        }
        return String.valueOf(it);
    }

    /**
     * 获取当前请求的用户名。
     * <p>
     * 注意：这个方法默认使用 system 用户名，如果存在认证信息，则使用认证用户名。
     */
    public static String currentUsername() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .flatMap(Authentications::findUsername)
                .orElse(SYSTEM_USERNAME);
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
