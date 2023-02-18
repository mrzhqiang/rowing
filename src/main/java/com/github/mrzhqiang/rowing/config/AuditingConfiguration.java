package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.rowing.util.Authentications;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 审计配置。
 * <p>
 * 参考：<a href="https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#auditing">Spring Data JPA 审计文档</a>
 */
@EnableJpaAuditing
@Configuration
public class AuditingConfiguration {

    @Bean
    public AuditorAware<String> auditor() {
        // 不使用 Authentications.ofLogin 方法，因为可能存在系统对实体的 CRUD 操作，需要支持未登录的用户名
        return () -> Authentications.ofCurrent().flatMap(Authentications::findUsername);
    }
}
