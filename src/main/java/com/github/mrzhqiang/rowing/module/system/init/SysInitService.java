package com.github.mrzhqiang.rowing.module.system.init;

import java.util.List;

/**
 * 系统初始化服务。
 */
public interface SysInitService {

    /**
     * 同步自动初始化器。
     *
     * @return 新发现的系统初始化列表，来自新创建的自动初始化器。
     */
    List<SysInit> syncAutoInitializer();
}
