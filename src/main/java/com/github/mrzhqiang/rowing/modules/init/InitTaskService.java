package com.github.mrzhqiang.rowing.modules.init;

/**
 * 初始化任务服务。
 */
public interface InitTaskService {

    /**
     * 同步数据。
     * <p>
     * 1. 新增的初始化任务实现类，首次同步；
     * <p>
     * 2. 数据库删除初始化任务后，重新同步；
     * <p>
     * 3. 删除的初始化任务实现类，标记废弃。
     *
     * @return 初始化任务同步数据。一般用来输出同步数据的分析报告。
     */
    InitTaskSyncData syncData();
}
