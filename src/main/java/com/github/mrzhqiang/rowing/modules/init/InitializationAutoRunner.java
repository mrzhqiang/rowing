package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.rowing.util.Authentications;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 初始化任务自动运行器。
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class InitializationAutoRunner implements ApplicationRunner {

    private final InitTaskService service;
    private final SecurityProperties properties;

    public InitializationAutoRunner(InitTaskService service,
                                    SecurityProperties properties) {
        this.service = service;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        // 初始化时，设置当前认证为 system 用户，保证审计字段正确录入
        // 同时拥有调用 rest 框架增强的 Repository 相关方法的权限
        Authentications.asSystem(properties);
        service.execute(args);
        // 执行完之后，切记清理安全上下文
        SecurityContextHolder.clearContext();
    }
}
