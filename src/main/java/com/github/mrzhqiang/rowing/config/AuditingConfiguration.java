package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.rowing.util.Authentications;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 审计配置。
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing">Spring 官方审计文档</a>
 */
@EnableJpaAuditing
@Configuration
public class AuditingConfiguration {

    @Bean
    public AuditorAware<String> auditor() {
        return () -> Authentications.ofLogin().flatMap(Authentications::findUsername);
    }
}
