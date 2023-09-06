package com.github.mrzhqiang.rowing.init;

import org.springframework.boot.ApplicationArguments;

/**
 * 初始化任务服务。
 */
public interface InitTaskService {

    /**
     * 跳过初始化任务同步的参数名称。
     */
    String SKIP_SYNC_ARGS_NAME = "init.task.sync.skip";
    /**
     * 跳过初始化任务自动执行的参数名称。
     */
    String SKIP_EXECUTE_ARGS_NAME = "init.task.execute.skip";

    /**
     * 同步数据。
     *
     * @param args 系统启动时的运行参数以及环境变量。
     */
    void sync(ApplicationArguments args);

    /**
     * 执行任务。
     *
     * @param args 系统启动时的运行参数以及环境变量。
     */
    void execute(ApplicationArguments args);

    /**
     * 通过路径执行初始化。
     *
     * @param path 初始化任务的唯一路径。
     */
    void executeByPath(String path);

}
