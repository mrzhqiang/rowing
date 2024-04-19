package com.github.mrzhqiang.rowing.role;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

/**
 * 角色事件处理器。
 */
@RepositoryEventHandler
@Component
@RequiredArgsConstructor
public class RoleEventHandle {

    private final RoleRepository repository;

    @HandleBeforeCreate
    public void onBeforeCreate(Role role) {
        // todo validate role
    }

    @HandleAfterCreate
    public void onAfterCreate(Role role) {
        // todo update something
    }

    @HandleBeforeSave
    public void onBeforeSave(Role role) {
        // todo validate role
    }

    @HandleAfterSave
    public void onAfterSave(Role role) {
        // do something
    }

}
