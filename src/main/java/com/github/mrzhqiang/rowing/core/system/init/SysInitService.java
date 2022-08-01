package com.github.mrzhqiang.rowing.core.system.init;

import com.github.mrzhqiang.rowing.core.system.AutoInitializer;

import java.util.List;

public interface SysInitService {

    /**
     * 检测自动初始化器列表。
     * <p>
     * 1. 通过 {@link AutoInitializer#getName()} 判断是否存在对应名称的系统初始化数据，如果存在则忽略
     * <p>
     * 2. 按照以上规则，得到未记录的自动初始化器列表，如果列表为空，则跳过检测，如果列表不为空，则执行下一步
     * <p>
     * 3. 逐个转换自动初始化器为系统初始化实体，并保存到数据库，完成检测操作
     *
     * @param initializerList 自动初始化器列表。
     * @return 已保存到数据库的系统初始化数据列表。
     */
    List<SysInit> check(List<AutoInitializer> initializerList);

    /**
     * 根据指定名称判断是否已完成初始化。
     *
     * @param name 名称字符串，一般是自动初始化实现类的全限定名称。
     * @return true 表示指定名称所代表的自动初始化器已执行完成，否则表示未完成。
     */
    boolean checkFinishedBy(String name);

    /**
     * 更新指定名称的实体状态为已完成。
     *
     * @param name 名称字符串。
     */
    void updateFinishedBy(String name);
}
