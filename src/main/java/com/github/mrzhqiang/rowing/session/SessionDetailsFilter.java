package com.github.mrzhqiang.rowing.session;

import com.github.mrzhqiang.rowing.util.Authentications;
import com.github.mrzhqiang.rowing.util.Splitters;
import com.google.common.base.Strings;
import com.google.common.net.HttpHeaders;
import eu.bitwalker.useragentutils.UserAgent;
import io.reactivex.observers.DefaultObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 会话详情过滤器。
 * <p>
 * 这个类来自 spring-session-sample-boot-findbyusername/src/main/java/sample/session/SessionDetailsFilter.java
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 101)
public class SessionDetailsFilter extends OncePerRequestFilter {

    /**
     * 未知的 IP 对应地址。
     */
    private static final String UNKNOWN = "(unknown-address)";
    /**
     * 访问类型模板。
     */
    private static final String ACCESS_TYPE_TEMPLATE = "%s -- %s";
    /**
     * 逗号分隔符。
     */
    private static final String COMMA = ",";

    private final SessionDetailsService service;

    public SessionDetailsFilter(SessionDetailsService service) {
        this.service = service;
    }

    @Override
    public void doFilterInternal(@Nonnull HttpServletRequest request,
                                 @Nonnull HttpServletResponse response,
                                 FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);

        HttpSession session = request.getSession(false);
        // 如果存在会话并且已经登录
        if (session != null && Authentications.ofLogin().isPresent()) {
            Object sessionDetails = session.getAttribute(Sessions.SESSION_DETAILS_KEY);
            // 基于会话的特性，不用每次访问都寻找会话详情
            if (sessionDetails == null) {
                findSessionDetail(request, session);
            }
        }
    }

    private void findSessionDetail(HttpServletRequest request, HttpSession session) {
        String userAgentText = request.getHeader(HttpHeaders.USER_AGENT);
        // User-Agent 里面东西太多太杂乱，我们只需要操作系统名称和浏览器名称即可
        UserAgent userAgent = UserAgent.parseUserAgentString(userAgentText);
        String osName = userAgent.getOperatingSystem().getName();
        String browserName = userAgent.getBrowser().getName();
        String accessType = Strings.lenientFormat(ACCESS_TYPE_TEMPLATE, osName, browserName);

        String remoteAddress = this.findRemoteAddress(request);
        service.observeApi(remoteAddress)
                // 网络不可用，那么使用本地数据库
                .onErrorResumeNext(service.observeDb(remoteAddress))
                .subscribe(new DefaultObserver<SessionDetails>() {
                    @Override
                    public void onNext(@Nonnull SessionDetails details) {
                        details.setAccessType(accessType);
                        session.setAttribute(Sessions.SESSION_DETAILS_KEY, details);
                    }

                    @Override
                    public void onError(@Nonnull Throwable e) {
                        // 记录错误日志是为了判断哪个更好用
                        log.error("无法为 {} 找到对应地址，可能是：{} 问题", remoteAddress, e.getLocalizedMessage());
                        SessionDetails details = new SessionDetails();
                        details.setIp(remoteAddress);
                        details.setLocation(UNKNOWN);
                        details.setAccessType(accessType);
                        session.setAttribute(Sessions.SESSION_DETAILS_KEY, details);
                    }

                    @Override
                    public void onComplete() {
                        // do nothing
                    }
                });
    }

    private String findRemoteAddress(HttpServletRequest request) {
        String remoteAddr = request.getHeader(HttpHeaders.X_FORWARDED_FOR);
        if (remoteAddr == null) {
            remoteAddr = request.getRemoteAddr();
        } else if (remoteAddr.contains(COMMA)) {
            remoteAddr = Splitters.COMMA.split(remoteAddr).iterator().next();
        }
        return remoteAddr;
    }
}
