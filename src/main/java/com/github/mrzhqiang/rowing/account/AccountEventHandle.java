package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.role.RoleService;
import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
import com.github.mrzhqiang.rowing.setting.Settings;
import com.github.mrzhqiang.rowing.user.UserService;
import com.github.mrzhqiang.rowing.util.Ranges;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.regex.Pattern;

import static com.github.mrzhqiang.rowing.domain.Domains.DEF_PASSWORD_MAX_LENGTH;
import static com.github.mrzhqiang.rowing.domain.Domains.DEF_USERNAME_REGEXP;
import static com.github.mrzhqiang.rowing.domain.Domains.PASSWORD_MIN_LENGTH;

/**
 * 账户事件处理器。
 */
@RepositoryEventHandler
@Component
@RequiredArgsConstructor
public class AccountEventHandle {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final SettingService settingService;

    @HandleBeforeCreate
    public void onBeforeCreate(Account account) {
        Preconditions.checkNotNull(account, "account == null");

        String username = account.getUsername();
        Accounts.validateUsername(username);
        // 用户名需要符合规则，TODO 规则从系统设置中读取
        boolean validUsername = Pattern.matches(DEF_USERNAME_REGEXP, username);
        // 用户名不存在
        boolean notExists = !repository.existsByUsername(username);
        Preconditions.checkArgument(validUsername && notExists, I18nHolder.getAccessor().getMessage(
                "Accounts.validUsername.invalid", "无效的用户名"));

        String password = account.getPassword();
        // 未设置密码，可能是系统创建的账户，则使用系统设置中的初始密码
        if (!StringUtils.hasText(password)) {
            password = settingService.findByCode(Settings.INIT_PASSWORD)
                    .map(Setting::getContent)
                    .orElse(Settings.DEF_INIT_PASSWORD);
        } else {
            // 注册时创建的账户，会携带明文密码，这里校验一下密码长度
            // 密码长度必须在范围内，TODO 密码最大长度从系统设置中读取，最大可设置的长度为 255（需要与实体字段长度保持一致）
            boolean validPasswordLength = Ranges.in(password.length(), PASSWORD_MIN_LENGTH, DEF_PASSWORD_MAX_LENGTH);
            Preconditions.checkArgument(validPasswordLength, I18nHolder.getAccessor().getMessage(
                    "Accounts.valid.passwordInvalid", "无效的密码"));
        }
        // 密码加密处理
        account.setPassword(passwordEncoder.encode(password));
        // 设置密码过期时间
        Duration passwordExpire = settingService.findByCode(Settings.PASSWORD_EXPIRE)
                .map(Setting::getContent)
                .map(DurationStyle::detectAndParse)
                .orElse(Settings.DEF_PASSWORD_EXPIRE);
        account.setPasswordExpired(Instant.now().plus(passwordExpire));
    }

    @HandleAfterCreate
    @HandleAfterSave
    public void onAfterCreateAndSave(Account account) {
        userService.binding(account);
        roleService.binding(account);
    }

}
