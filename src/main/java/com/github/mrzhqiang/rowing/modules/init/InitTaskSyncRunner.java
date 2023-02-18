package com.github.mrzhqiang.rowing.modules.init;

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
 * 初始化任务同步运行器。
 * <p>
 * 这个类需要尽可能早地执行，所以执行的优先级最高。
 *
 * @see ApplicationRunner 应用运行器接口，用来在系统启动时自动执行。
 * @see Order 排序注解，用来定义执行时的优先级。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class InitTaskSyncRunner implements ApplicationRunner {

    private final InitTaskService service;
    private final SecurityProperties properties;

    public InitTaskSyncRunner(InitTaskService service,
                              SecurityProperties properties) {
        this.service = service;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        // 设置
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        String name = properties.getUser().getName();
        String password = properties.getUser().getPassword();
        context.setAuthentication(UsernamePasswordAuthenticationToken.authenticated(name, password,
                AuthorityUtils.createAuthorityList(properties.getUser().getRoles().toArray(new String[0]))));
        SecurityContextHolder.setContext(context);

        service.syncData(args);

        SecurityContextHolder.clearContext();
    }
}
