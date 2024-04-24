package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.dict.gbt.DictGBTService;
import com.github.mrzhqiang.rowing.dict.iso.DictISOService;
import com.github.mrzhqiang.rowing.domain.TaskMode;
import com.github.mrzhqiang.rowing.init.AutoInitializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 字典自动初始化器。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DictAutoInitializer extends AutoInitializer {

    private final DictService service;
    private final DictISOService isoService;
    private final DictGBTService gbtService;

    @Override
    protected void onExecute() {
        service.sync();
        isoService.sync();
        gbtService.sync();
    }

    @Override
    public TaskMode getMode() {
        // 每次启动都执行
        return TaskMode.EACH;
    }

}
