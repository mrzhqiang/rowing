package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.action.Action;
import com.github.mrzhqiang.rowing.domain.ActionType;
import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
import com.github.mrzhqiang.rowing.setting.Settings;
import com.github.mrzhqiang.rowing.util.Authentications;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

/**
 * 登录失败监听器。
 * <p>
 * 监听抽象的认证失败事件，通过不同的认证失败事件，进行不同的处理逻辑。
 */
@Component
public class LoginFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private final AccountService accountService;
    private final SettingService settingService;

    public LoginFailureListener(AccountService accountService,
                                SettingService settingService) {
        this.accountService = accountService;
        this.settingService = settingService;
    }

    /**
     * 注意：通常是先发出登录失败的事件，再执行登录失败的回调。
     */
    @Action(value = "登录失败处理", type = ActionType.LOGIN)
    @RunAsSystem
    @Override
    public void onApplicationEvent(@Nonnull AbstractAuthenticationFailureEvent event) {
        Authentication authentication = event.getAuthentication();
        if (event instanceof AuthenticationFailureBadCredentialsEvent
                || event instanceof AuthenticationFailureLockedEvent) {
            handleLoginFailed(authentication);
        }
    }

    private void handleLoginFailed(Authentication authentication) {
        Authentications.findUsername(authentication)
                .flatMap(accountService::findByUsername)
                .map(this::computeFailedCount)
                .ifPresent(accountService::update);
    }

    private Account computeFailedCount(Account account) {
        int hasFailedCount = account.getFailedCount();
        if (account.isAccountNonLocked()) {
            int maxLoginFailed = settingService.findByCode(Settings.MAX_LOGIN_FAILED)
                    .map(Setting::getContent)
                    .map(Integer::parseInt)
                    .orElse(Settings.DEF_MAX_LOGIN_FAILED);
            if (hasFailedCount < maxLoginFailed) {
                account.setFailedCount(hasFailedCount + 1);
            }
            if (account.getFailedCount() >= maxLoginFailed) {
                Duration duration = settingService.findByCode(Settings.ACCOUNT_LOCKED_DURATION)
                        .map(Setting::getContent)
                        .map(DurationStyle::detectAndParse)
                        .orElse(Settings.DEF_ACCOUNT_LOCKED_DURATION);
                account.setLocked(Instant.now().plus(duration));
            }
            return account;
        }

        // 锁定的账户，如果失败次数未重置为零，则进行重置操作
        if (hasFailedCount != 0) {
            account.setFailedCount(0);
            return account;
        }
        // 返回 null 值不会进行更新操作
        return null;
    }

}
