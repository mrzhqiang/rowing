package com.github.mrzhqiang.rowing.core.session;

import org.springframework.web.context.request.RequestAttributes;
import static org.springframework.web.context.request.RequestAttributes.REFERENCE_SESSION;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_SESSION;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Nullable;
import javax.servlet.http.HttpSession;

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
    public static final String SESSION_DETAILS_KEY = "SESSION_DETAILS";

    /**
     * 当前会话。
     *
     * @return 返回当前会话实例。如果会话不存在，则返回 null 值。
     */
    @Nullable
    public static HttpSession ofCurrent() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return (HttpSession) requestAttributes.getAttribute(REFERENCE_SESSION, SCOPE_SESSION);
    }

    /**
     * 当前会话详情。
     *
     * @return 返回当前会话详情。如果会话不存在，则会话详情也将不存在；如果会话存在，则查看会话中的会话详情属性是否存在对应的值。
     */
    @Nullable
    public static SessionDetails ofCurrentDetails() {
        HttpSession session = Sessions.ofCurrent();
        if (session != null) {
            // 通过 SessionDetailsFilter 存放的会话详情，我们在这里尝试获取存放的实例
            Object details = session.getAttribute(SESSION_DETAILS_KEY);
            if (details != null) {
                return ((SessionDetails) details);
            }
        }
        return null;
    }
}
