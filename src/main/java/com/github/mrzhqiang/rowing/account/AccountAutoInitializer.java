package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.system.init.BaseAutoInitializer;
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
public class AccountAutoInitializer extends BaseAutoInitializer {

    private final AccountService service;

    public AccountAutoInitializer(AccountService service) {
        this.service = service;
    }

    @Override
    public void execute() throws Exception {
        service.initAdmin();

    }

}
