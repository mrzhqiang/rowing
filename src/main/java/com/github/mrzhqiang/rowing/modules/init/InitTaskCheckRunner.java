package com.github.mrzhqiang.rowing.modules.init;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
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
 * @see ApplicationRunner 应用运行器，表示在系统启动时自动执行。
 * @see Order 排序注解，表示定义执行优先级。在优先级相同的情况下，通常比 {@link org.springframework.boot.CommandLineRunner} 先一步执行。
 * @see Ordered 排序接口，表示定义执行优先级。
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class InitTaskCheckRunner implements ApplicationRunner {

    /**
     * 跳过初始化检测的参数名称。
     * <p>
     * 使用 --skip_init_check=true 可以跳过初始化检测运行器的运行。
     */
    public static final String SKIP_ARGS_NAME = "skip_init_check";

    private final InitTaskService initTaskService;
    private final MessageSourceAccessor sourceAccessor;

    public InitTaskCheckRunner(InitTaskService initTaskService,
                               MessageSource messageSource) {
        this.initTaskService = initTaskService;
        this.sourceAccessor = new MessageSourceAccessor(messageSource);
    }

    @Override
    public void run(ApplicationArguments args) {
        // 从运行参数或环境变量中判断是否包含 --skip_init_check
        if (args.containsOption(SKIP_ARGS_NAME)) {
            // 解析选项参数，如果是 --skip_init_check=true 表示跳过检测
            List<String> optionValues = args.getOptionValues(SKIP_ARGS_NAME);
            if (!CollectionUtils.isEmpty(optionValues)
                    && optionValues.contains(Boolean.TRUE.toString())) {
                String skipMessage = sourceAccessor.getMessage(
                        "InitTaskCheckRunner.run.skip_init_check",
                        "初始化任务检测：发现 skip_init_check=true 跳过检测");
                log.info(skipMessage);
                return;
            }
        }

        InitTaskSyncData syncData = initTaskService.syncData();
        List<InitTask> addedList = syncData.getAddedList();
        List<InitTask> discardList = syncData.getDiscardList();
        String reportMessage = sourceAccessor.getMessage("InitTaskCheckRunner.run.report",
                new Object[]{addedList.size(), discardList.size()},
                Strings.lenientFormat("初始化任务检测：新增 %s 个初始化任务，废弃 %s 初始化任务",
                        addedList.size(), discardList.size()));
        log.info(reportMessage);
    }
}
