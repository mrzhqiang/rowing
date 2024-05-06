package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.account.RunAsSystem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化运行器。
 * <p>
 * 在系统启动时自动执行初始化任务。
 * <p>
 * 注意：这个运行器的优先级必须最高。
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@RequiredArgsConstructor
public class InitializationRunner implements ApplicationRunner {

    private final InitTaskService service;

    @RunAsSystem
    @Override
    public void run(ApplicationArguments args) {
        try {
            service.sync(args);
            service.execute(args);
        } catch (Exception exception) {
            log.error("初始化运行器运行出错！系统即将退出...", exception);
            throw exception;
        }
    }

}
