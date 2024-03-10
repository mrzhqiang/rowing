package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.menu.MenuResource;
import com.github.mrzhqiang.rowing.role.Role;
import com.github.mrzhqiang.rowing.role.RoleService;
import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
import com.github.mrzhqiang.rowing.setting.Settings;
import com.github.mrzhqiang.rowing.user.UserService;
import com.github.mrzhqiang.rowing.util.Authorizes;
import com.github.mrzhqiang.rowing.util.Ranges;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.mrzhqiang.rowing.domain.Domains.DEF_PASSWORD_MAX_LENGTH;
import static com.github.mrzhqiang.rowing.domain.Domains.PASSWORD_MIN_LENGTH;
import static com.github.mrzhqiang.rowing.domain.Domains.USERNAME_MAX_LENGTH;
import static com.github.mrzhqiang.rowing.domain.Domains.USERNAME_MIN_LENGTH;
import static com.github.mrzhqiang.rowing.domain.Domains.USERNAME_REGEXP;

/**
 * 账户事件处理器。
 */
@RepositoryEventHandler
@Component
public class AccountEventHandle {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final SettingService settingService;

    public AccountEventHandle(AccountRepository repository,
                              PasswordEncoder passwordEncoder,
                              UserService userService,
                              RoleService roleService,
                              SettingService settingService) {
        this.repository = repository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.settingService = settingService;
    }

    @HandleBeforeCreate
    public void onBeforeCreate(Account account) {
        Preconditions.checkNotNull(account, "account == null");
        String username = account.getUsername();
        Accounts.validUsername(username);
        // 用户名需要符合规则，TODO 规则从系统设置中读取
        boolean validUsername = Pattern.matches(USERNAME_REGEXP, username);
        // 用户名不存在
        boolean notExists = !repository.existsByUsername(username);
        Preconditions.checkArgument(validUsername && notExists,
                I18nHolder.getAccessor().getMessage("Accounts.validUsername.invalid", "无效的用户名"));
        String password = account.getPassword();
        // 密码长度必须在范围内，TODO 密码最大长度从系统设置中读取，最大可设置的长度为 255
        boolean validPasswordLength = Ranges.in(password.length(), PASSWORD_MIN_LENGTH, DEF_PASSWORD_MAX_LENGTH);
        Preconditions.checkArgument(validPasswordLength,
                I18nHolder.getAccessor().getMessage("Accounts.valid.passwordInvalid", "无效的密码"));
        // 未设置密码，则使用系统设置中的初始密码
        if (!StringUtils.hasText(password)) {
            password = settingService.findByCode(Settings.INIT_PASSWORD)
                    .map(Setting::getContent)
                    .orElse(Settings.DEF_INIT_PASSWORD);
        }
        // 密码编码处理
        account.setPassword(passwordEncoder.encode(password));
        // 设置密码过期时间
        Duration passwordExpire = settingService.findByCode(Settings.PASSWORD_EXPIRE)
                .map(Setting::getContent)
                .map(DurationStyle::detectAndParse)
                .orElse(Settings.DEF_PASSWORD_EXPIRE);
        account.setPasswordExpired(Instant.now().plus(passwordExpire));
        updateAuthority(account);
    }

    private void updateAuthority(Account account) {
        String authority = roleService.findAllBy(account).stream()
                .flatMap(it -> Stream.concat(Stream.of(it.getCode()),
                        it.getMenuResources().stream().map(MenuResource::getAuthority)))
                .distinct()
                .collect(Collectors.joining(Authorizes.SPLIT_CHAR));
        account.setAuthority(authority);
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
