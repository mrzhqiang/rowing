package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.init.AutoInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 角色自动初始化器
 * <p>
 * 用于自动初始化角色数据。
 */
@Component
@RequiredArgsConstructor
public class RoleAutoInitializer extends AutoInitializer {

    private final RoleService service;

    @Override
    protected void onExecute() {
        service.init();
    }

}
