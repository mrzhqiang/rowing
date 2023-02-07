package com.github.mrzhqiang.rowing.modules.account;

import com.github.mrzhqiang.rowing.modules.init.AutoInitializer;
import com.github.mrzhqiang.rowing.modules.init.BaseInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 账号自动初始化器。
 * <p>
 * 主要进行与账号有关的初始化。
 * <p>
 * 1. 角色初始化。
 * <p>
 * 2. 管理员账号初始化。
 */
@Slf4j
@Component
public class AccountInitializer extends AutoInitializer {

    private final AccountService service;

    public AccountInitializer(AccountService service) {
        this.service = service;
    }

    @Override
    public void execute() {
        service.initAdmin();

    }

}
