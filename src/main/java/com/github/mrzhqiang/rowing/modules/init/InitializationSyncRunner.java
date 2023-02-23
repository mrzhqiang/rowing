package com.github.mrzhqiang.rowing.modules.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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
public final class InitializationSyncRunner implements ApplicationRunner {

    private final InitTaskService service;

    public InitializationSyncRunner(InitTaskService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {
        service.syncData(args);
    }
}
