package com.github.mrzhqiang.rowing.modules.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 初始化任务自动运行器。
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class InitTaskAutoRunner implements ApplicationRunner {

    private final InitTaskService service;
    private final SecurityProperties properties;

    public InitTaskAutoRunner(InitTaskService service,
                              SecurityProperties properties) {
        this.service = service;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        String name = properties.getUser().getName();
        String password = properties.getUser().getPassword();
        context.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(name, password,
                AuthorityUtils.createAuthorityList(properties.getUser().getRoles().toArray(new String[0]))));
        SecurityContextHolder.setContext(context);

        service.execute(args);

        SecurityContextHolder.clearContext();
    }
}
