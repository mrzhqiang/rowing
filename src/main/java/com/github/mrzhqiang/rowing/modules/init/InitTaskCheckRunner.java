package com.github.mrzhqiang.rowing.modules.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 初始化任务检测运行器。
 * <p>
 * 主要将 Spring 容器内的 {@link Initializer} 实现转为 {@link InitTask} 并同步到数据库。
 *
 * @see ApplicationRunner 表示在系统启动时自动执行。
 * @see Order 表示定义执行优先级。在优先级相同的情况下，通常比 {@link org.springframework.boot.CommandLineRunner} 先一步执行。
 * @see Ordered 同上，表示定义执行优先级。
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class InitTaskCheckRunner implements ApplicationRunner {

    public static final String SKIP_ARGS_NAME = "skip_init_check";

    private final InitTaskService initTaskService;

    public InitTaskCheckRunner(InitTaskService initTaskService) {
        this.initTaskService = initTaskService;
    }

    @Override
    public void run(ApplicationArguments args) {
        // 判断运行参数或环境变量是否包含 --skip_init_check
        if (args.containsOption(SKIP_ARGS_NAME)) {
            // 解析如果是 --skip_init_check=true 表示跳过检测
            List<String> optionValues = args.getOptionValues(SKIP_ARGS_NAME);
            if (!CollectionUtils.isEmpty(optionValues)
                    && optionValues.contains(Boolean.TRUE.toString())) {
                log.info("初始化任务检测：发现 skip_init_check=true 跳过检测");
                return;
            }
        }

        InitTaskSyncData syncData = initTaskService.syncData();
        String report = syncData.report();
        log.info("初始化任务检测：{}", report);
    }
}
