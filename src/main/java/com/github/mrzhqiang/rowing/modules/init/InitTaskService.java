package com.github.mrzhqiang.rowing.modules.init;

/**
 * 初始化任务服务。
 */
public interface InitTaskService {

    /**
     * 同步数据。
     *
     * @return 初始化任务同步数据，主要是展示同步情况。
     */
    InitTaskSyncData syncData();
}
