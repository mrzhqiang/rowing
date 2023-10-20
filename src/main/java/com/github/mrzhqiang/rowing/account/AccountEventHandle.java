package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
import com.github.mrzhqiang.rowing.user.UserService;
import com.google.common.base.Strings;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 账户事件处理器。
 */
@RepositoryEventHandler
@Component
public class AccountEventHandle {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final SettingService settingService;

    public AccountEventHandle(AccountRepository repository,
                              UserService userService,
                              PasswordEncoder passwordEncoder,
                              SettingService settingService) {
        this.repository = repository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.settingService = settingService;
    }

    @HandleBeforeCreate
    public void onBeforeCreate(Account account) {
        if (repository.findByUsername(account.getUsername()).isPresent()) {
            throw new RuntimeException(Strings.lenientFormat(
                    "无效的用户名 %s，请换一个再试", account.getUsername()));
        }
        String password = account.getPassword();
        if (!StringUtils.hasText(password)) {
            password = settingService.findByCode("accountInitPassword")
                    .map(Setting::getContent)
                    .orElse("123456");
        }
        account.setPassword(passwordEncoder.encode(password));
    }

    @HandleAfterCreate
    public void onAfterCreate(Account account) {
        userService.binding(account);
    }

    @HandleBeforeSave
    public void onBeforeSave(Account account) {
        // do something
    }

    @HandleBeforeDelete
    public void onBeforeDelete(Account account) {
        // do something
    }

}
