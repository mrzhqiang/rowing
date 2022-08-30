package com.github.mrzhqiang.rowing.core.init;

import com.github.mrzhqiang.rowing.core.account.AccountService;
import com.github.mrzhqiang.rowing.core.account.RegisterForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountAutoInitializer extends BaseAutoInitializer {

    private final AccountService service;

    public AccountAutoInitializer(AccountService service) {
        this.service = service;
    }

    @Override
    public void execute() throws Exception {
        RegisterForm form = new RegisterForm();
        form.setUsername(AccountService.ADMIN_USERNAME);

    }

}
