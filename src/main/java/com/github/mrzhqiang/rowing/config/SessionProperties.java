package com.github.mrzhqiang.rowing.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@ConfigurationProperties("rowing.session")
public class SessionProperties {

    /**
     * 默认的最大会话数量。
     * <p>
     * 超过最大会话数量，将自动掉线处理。
     */
    private static final int DEF_MAX_SESSION = 1;

    /**
     * 每个账号最多能登录的会话数量。
     */
    private Integer maxSession = DEF_MAX_SESSION;
    /**
     * 限流器参数。
     */
    private RateLimiter rateLimiter;

    @Getter
    @Setter
    @ToString
    public static class RateLimiter {

        /**
         * 默认的限流器键名称。
         * <p>
         * 这个键名称将在会话中保持，以便计算每秒接口访问次数，得出限流数据。
         */
        public static final String DEF_RATE_LIMITER_KEY = "RateLimiter";
        /**
         * 默认的每秒限流许可。
         * <p>
         * 即表示在此许可范围内，接口访问是允许的，否则将触发限流策略。
         */
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
