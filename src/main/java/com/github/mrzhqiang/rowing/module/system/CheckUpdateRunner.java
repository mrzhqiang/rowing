package com.github.mrzhqiang.rowing.module.system;

import com.github.mrzhqiang.rowing.module.system.data.DataDictService;
import com.github.mrzhqiang.rowing.domain.DataDictType;
import com.github.mrzhqiang.rowing.module.system.init.AutoInitializer;
import com.github.mrzhqiang.rowing.module.system.init.SysInit;
import com.github.mrzhqiang.rowing.module.system.init.SysInitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 检测更新运行器。
 * <p>
 * 这个运行器只在系统启动时运行，优先级最高，主要执行以下任务：
 * <p>
 * 1. 检测内置数据字典：扫描所有的数据字典枚举，将其作为 {@link DataDictType#INTERNAL 内置数据字典} 同步到数据库。
 * <p>
 * 2. 检测系统初始化：基于 Spring 容器获得 {@link AutoInitializer 自动初始化} 的所有实现类，并同步到数据库。
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public final class CheckUpdateRunner implements ApplicationRunner {

    private final DataDictService dataDictService;
    private final SysInitService sysInitService;

    public CheckUpdateRunner(DataDictService dataDictService,
                             SysInitService sysInitService) {
        this.dataDictService = dataDictService;
        this.sysInitService = sysInitService;
    }

    @Override
    public void run(ApplicationArguments args) {
        checkDataDict();
        checkSysInit();
    }

    private void checkDataDict() {
        String basePackage = DataDictType.class.getPackage().getName();
        if (log.isDebugEnabled()) {
            log.debug("准备同步 {} 包下的数据字典枚举", basePackage);
        }
        dataDictService.syncInternal(basePackage);
    }

    private void checkSysInit() {
        List<SysInit> syncList = sysInitService.syncAutoInitializer();
        if (CollectionUtils.isEmpty(syncList)) {
            log.info("检测完毕！未发现有新的自动初始化器");
        } else {
            log.info("检测完毕！发现有 {} 个新的自动初始化器，已记录到数据库", syncList.size());
        }
    }
}
