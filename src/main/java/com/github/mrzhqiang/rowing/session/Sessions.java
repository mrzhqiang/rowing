package com.github.mrzhqiang.rowing.session;

import org.springframework.web.context.request.RequestAttributes;
import static org.springframework.web.context.request.RequestAttributes.REFERENCE_SESSION;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * 会话工具。
 */
public final class Sessions {
    private Sessions() {
        // no instances
    }

    /**
     * 会话详情的键名称，存储在会话属性中。
     */
    public static final String SESSION_DETAILS_KEY = Sessions.class.getName() + ".SESSION_DETAILS";

    /**
     * 当前会话。
     *
     * @return 返回可选的当前会话。
     */
    public static Optional<HttpSession> ofCurrent() {
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes) {
                return Optional.of(requestAttributes)
                        .map(it -> ((ServletRequestAttributes) it))
                        .map(ServletRequestAttributes::getRequest)
                        .map(it -> it.getSession(false));
            }
            return Optional.of(requestAttributes)
                    .map(it -> it.getAttribute(REFERENCE_SESSION, SCOPE_SESSION))
                    .map(it -> (HttpSession) it);
        } catch (IllegalStateException ignored) {
            return Optional.empty();
        }
    }

    /**
     * 当前会话详情。
     *
     * @return 返回当前会话详情。如果会话不存在，则会话详情也将不存在；如果会话存在，则查看会话中的会话详情属性是否存在对应的值。
     */
    public static Optional<SessionDetails> ofCurrentDetails() {
        return Sessions.ofCurrent()
                // 通过 SessionDetailsFilter 存放的会话详情，我们在这里尝试获取存放的实例
                .map(it -> it.getAttribute(SESSION_DETAILS_KEY))
                .map(it -> (SessionDetails) it);
    }
}
