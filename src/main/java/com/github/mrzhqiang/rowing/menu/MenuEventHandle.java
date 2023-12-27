package com.github.mrzhqiang.rowing.menu;

import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

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

    @Transactional(rollbackFor = Exception.class)
    @HandleBeforeSave
    public void onBeforeSave(Menu menu) {
        Menus.validateSave(menu);
        Menus.handleName(menu);
        Menus.handleFullPath(menu);

        List<Menu> children = menu.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        children.forEach(this::updateFullPath);
    }

    private void updateFullPath(Menu menu) {
        Menus.handleFullPath(menu);
        repository.save(menu);

        List<Menu> children = menu.getChildren();
        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        children.forEach(this::updateFullPath);
    }

    @HandleBeforeDelete
    public void onBeforeDelete(Menu menu) {
        Menus.validateDelete(menu);
    }

}
