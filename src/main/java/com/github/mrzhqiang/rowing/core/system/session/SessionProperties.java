package com.github.mrzhqiang.rowing.core.system.session;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@ConfigurationProperties("session")
public class SessionProperties {

    private static final int DEF_MAX_SESSION = 1;
    private static final String DEF_EXPIRED_PATH = "/login?expired";

    /**
     * 每个账号最多能登录的会话数量。
     */
    private Integer maxSession = DEF_MAX_SESSION;
    /**
     * 会话过期重定向地址。
     * <p>
     * 可能是由于会话数量过多而引起会话过期。
     */
    private String expiredPath = DEF_EXPIRED_PATH;
    /**
     * 限流器参数。
     */
    private RateLimiter rateLimiter;

    @Getter
    @Setter
    @ToString
    public static class RateLimiter {

        public static final String DEF_RATE_LIMITER_KEY = "RateLimiter";
        public static final double DEF_RATE_LIMITER_PERMITS = 2.0;

        /**
         * 会话中持有的限流器 KEY 名称。
         */
        private String keyName = DEF_RATE_LIMITER_KEY;
        /**
         * 许可：平均每秒访问次数不超过 N 次。
         */
        private Double permits = DEF_RATE_LIMITER_PERMITS;
    }
}
