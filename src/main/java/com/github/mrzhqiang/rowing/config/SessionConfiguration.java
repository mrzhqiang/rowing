package com.github.mrzhqiang.rowing.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 会话配置。
 */
@EnableRedisHttpSession
@EnableConfigurationProperties({SessionProperties.class})
@Configuration
public class SessionConfiguration {
}
