package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.helper.random.RandomNumbers;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
import com.github.mrzhqiang.rowing.user.User;
import com.github.mrzhqiang.rowing.user.UserRepository;
import com.google.common.base.Strings;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SettingService settingService;

    public AccountEventHandle(AccountRepository repository,
                              UserRepository userRepository,
                              PasswordEncoder passwordEncoder,
                              SettingService settingService) {
        this.repository = repository;
        this.userRepository = userRepository;
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
        String nickname = settingService.findByCode("userNicknamePrefix")
                .map(Setting::getContent)
                .orElse("用户" + RandomNumbers.rangeInt(6, Domains.USER_NICKNAME_LENGTH - 2));
        User user = User.builder()
                .nickname(nickname)
                .owner(account)
                .build();
        userRepository.save(user);
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
