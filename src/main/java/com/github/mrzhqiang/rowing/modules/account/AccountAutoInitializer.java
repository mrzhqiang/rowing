package com.github.mrzhqiang.rowing.modules.account;

import com.github.mrzhqiang.rowing.modules.init.AutoInitializer;
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
public class AccountAutoInitializer extends AutoInitializer {

    private final AccountService service;

    public AccountAutoInitializer(AccountService service) {
        this.service = service;
    }

    @Override
    protected void autoRun() throws Exception {
        service.initAdmin();
    }

    @Override
    public boolean isSupportRepeat() {
        return false;
    }
}
