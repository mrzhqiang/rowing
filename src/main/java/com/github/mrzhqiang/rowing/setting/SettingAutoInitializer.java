package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.init.AutoInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 设置自动初始化器。
 */
@Slf4j
@Component
public class SettingAutoInitializer extends AutoInitializer {

    private final SettingService service;

    public SettingAutoInitializer(SettingService service) {
        this.service = service;
    }

    @Override
    protected void onExecute() {
        service.init();
    }

}
