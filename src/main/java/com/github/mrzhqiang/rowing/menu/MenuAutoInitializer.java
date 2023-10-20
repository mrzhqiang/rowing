package com.github.mrzhqiang.rowing.menu;

import com.github.mrzhqiang.rowing.init.AutoInitializer;
import org.springframework.stereotype.Component;

/**
 * 菜单自动初始化器。
 */
@Component
public class MenuAutoInitializer extends AutoInitializer {

    private final MenuService service;

    public MenuAutoInitializer(MenuService service) {
        this.service = service;
    }

    @Override
    protected void onExecute() {
        service.init();
    }

}
