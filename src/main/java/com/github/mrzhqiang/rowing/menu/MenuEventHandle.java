package com.github.mrzhqiang.rowing.menu;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * 菜单事件处理器。
 * <p>
 * 处理菜单的各种事件，比如创建前、创建后、保存前、保存后、删除前、删除后等等。
 */
@RepositoryEventHandler
@Component
public class MenuEventHandle {

    private final MenuRepository repository;

    public MenuEventHandle(MenuRepository repository) {
        this.repository = repository;
    }

    @HandleBeforeCreate
    public void onBeforeCreate(Menu menu) {
        Menus.validateCreate(menu);
        Menus.handleName(menu);
        Menus.handleFullPath(menu);
    }

    @HandleBeforeSave
    public void onBeforeSave(Menu menu) {

    }

    @HandleBeforeDelete
    public void onBeforeDelete(Menu menu) {
        Menus.validateDelete(menu);
    }

}
