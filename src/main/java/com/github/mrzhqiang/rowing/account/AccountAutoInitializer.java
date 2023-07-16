package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.init.AutoInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 账号自动初始化器。
 */
@Slf4j
@Component
public class AccountAutoInitializer extends AutoInitializer {

    private final AccountService service;

    public AccountAutoInitializer(AccountService service) {
        this.service = service;
    }

    @RunAsSystem
    @Override
    protected void onExecute() {
        service.init();
    }

}
