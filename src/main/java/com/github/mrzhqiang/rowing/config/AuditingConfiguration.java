package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.rowing.util.Authentications;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * 审计配置。
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing">Spring Data JPA 审计文档</a>
 */
@EnableJpaAuditing
@Configuration
public class AuditingConfiguration {

    /**
     * 用来填充审计字段。
     *
     * @see org.springframework.data.annotation.CreatedBy
     * @see org.springframework.data.annotation.LastModifiedBy
     */
    @Bean
    public AuditorAware<String> auditor() {
        // 默认情况下返回 system 用户名称，如果存在已认证用户，则返回已认证用户名称
        return () -> Optional.of(Authentications.ofLogin()
                .flatMap(Authentications::findUsername)
                .orElse(Authentications.SYSTEM_USERNAME));
    }
}
