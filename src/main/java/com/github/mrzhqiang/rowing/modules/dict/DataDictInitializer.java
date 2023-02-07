package com.github.mrzhqiang.rowing.modules.dict;

import com.github.mrzhqiang.rowing.domain.DataDictType;
import com.github.mrzhqiang.rowing.modules.init.AutoInitializer;
import com.github.mrzhqiang.rowing.modules.init.BaseInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 字典自动初始化执行器。
 * <p>
 * 用于在系统启动时，自动执行字典初始化任务，包括获取指定位置下的所有字典枚举，同步到数据库中作为内置字典。
 */
@Slf4j
@Component
public final class DataDictInitializer extends AutoInitializer {

    private final DataDictService service;

    public DataDictInitializer(DataDictService service) {
        this.service = service;
    }

    @Override
    public void execute() {
        String basePackage = DataDictType.class.getPackage().getName();
        if (log.isDebugEnabled()) {
            log.debug("准备同步 {} 包下的数据字典枚举", basePackage);
        }
        service.syncInternal(basePackage);
    }
}
