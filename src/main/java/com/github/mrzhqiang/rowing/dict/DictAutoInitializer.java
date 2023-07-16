package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.account.RunAsSystem;
import com.github.mrzhqiang.rowing.domain.TaskMode;
import com.github.mrzhqiang.rowing.init.AutoInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 字典自动初始化器。
 */
@Slf4j
@Component
public class DictAutoInitializer extends AutoInitializer {

    private final DictService service;

    public DictAutoInitializer(DictService service) {
        this.service = service;
    }

    @RunAsSystem
    @Override
    protected void onExecute() {
        service.sync();
    }

    @Override
    public TaskMode getMode() {
        // 每次启动都执行
        return TaskMode.EACH;
    }

}
