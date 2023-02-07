package com.github.mrzhqiang.rowing.modules.init;

import lombok.Getter;

import java.util.List;

/**
 * 初始化任务检测数据。
 * <p>
 */
@Getter
public class InitTaskSyncData {

    private final List<InitTask> addedList;
    private final List<InitTask> discardList;

    private InitTaskSyncData(List<InitTask> addedList, List<InitTask> discardList) {
        this.addedList = addedList;
        this.discardList = discardList;
    }

    public static InitTaskSyncData of(List<InitTask> addedList, List<InitTask> discardList) {
        return new InitTaskSyncData(addedList, discardList);
    }
}
