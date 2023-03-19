package com.github.mrzhqiang.rowing.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化任务自动运行器。
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class InitializationAutoRunner implements ApplicationRunner {

    private final InitTaskService service;

    public InitializationAutoRunner(InitTaskService service) {
        this.service = service;
    }

    @Override
    public void run(ApplicationArguments args) {
        service.execute(args);
    }
}
