package com.github.mrzhqiang.rowing.modules.init;

import java.util.List;
import java.util.Optional;

/**
 * 初始化任务检测数据。
 * <p>
 */
public class InitTaskSyncData {

    // todo i18n
    public static final String ADDED_TEMPLATE = "新增 %s 条初始化任务数据";
    public static final String DISCARD_TEMPLATE = "废弃 %s 条初始化任务数据";

    private static final InitTaskSyncData EMPTY = new InitTaskSyncData();

    private final List<InitTask> added;
    private final List<InitTask> discard;

    private InitTaskSyncData() {
        // 禁止外部实例化，只能通过静态工厂方法生成数据
        this.added = null;
        this.discard = null;
    }

    private InitTaskSyncData(List<InitTask> added, List<InitTask> discard) {
        this.added = added;
        this.discard = discard;
    }

    public static InitTaskSyncData empty() {
        return EMPTY;
    }

    public static InitTaskSyncData of(List<InitTask> added, List<InitTask> discard) {
        return new InitTaskSyncData(added, discard);
    }

    public String report() {
        int addTotal = Optional.ofNullable(added).map(List::size).orElse(0);
        int discardTotal = Optional.ofNullable(added).map(List::size).orElse(0);
        return "";
    }
}
