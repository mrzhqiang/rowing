package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.TaskMode;
import com.github.mrzhqiang.rowing.init.AutoInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 字典自动初始化执行器。
 */
@Slf4j
@Component
@EnableConfigurationProperties(DictProperties.class)
public class DictAutoInitializer extends AutoInitializer {

    private final DictService service;
    private final DictProperties properties;

    public DictAutoInitializer(DictService service, DictProperties properties) {
        this.service = service;
        this.properties = properties;
    }

    @Override
    protected void onExecute() {
        List<String> innerPaths = properties.getInnerPaths();
        if (!CollectionUtils.isEmpty(innerPaths)) {
            innerPaths.forEach(service::syncInternal);
        }

        List<String> excelPaths = properties.getExcelPaths();
        if (!CollectionUtils.isEmpty(excelPaths)) {
            excelPaths.forEach(service::syncExcel);
        }
    }

    @Override
    public TaskMode getMode() {
        // 每次启动都执行
        return TaskMode.EACH;
    }
}
