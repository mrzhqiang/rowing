package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.init.AutoInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 菜单自动初始化器。
 */
@Component
@RequiredArgsConstructor
public class MenuAutoInitializer extends AutoInitializer {

    private final MenuService service;

    @Override
    protected void onExecute() {
        service.init();
    }

}
