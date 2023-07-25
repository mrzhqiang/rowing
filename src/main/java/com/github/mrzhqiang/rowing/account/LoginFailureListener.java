package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
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

@Component
public class LoginFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private static final int DEF_MAX_LOGIN_FAILED = 5;
    private static final Duration DEF_ACCOUNT_LOCKED_DURATION = Duration.ofMinutes(30);

    private static final String MAX_LOGIN_FAILED = "max-login-failed";
    private static final String ACCOUNT_LOCKED_DURATION = "account-locked-duration";

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
                .map(accountService::loadUserByUsername)
                .map(this::computeFailedCount)
                .ifPresent(accountService::update);
    }

    private Account computeFailedCount(Account account) {
        int hasFailedCount = account.getFailedCount();
        if (account.isAccountNonLocked()) {
            int maxLoginFailed = settingService.findByName(MAX_LOGIN_FAILED)
                    .map(Setting::getContent)
                    .map(Integer::parseInt)
                    .orElse(DEF_MAX_LOGIN_FAILED);
            if (hasFailedCount < maxLoginFailed) {
                account.setFailedCount(hasFailedCount + 1);
            }
            if (account.getFailedCount() >= maxLoginFailed) {
                Duration duration = settingService.findByName(ACCOUNT_LOCKED_DURATION)
                        .map(Setting::getContent)
                        .map(DurationStyle::detectAndParse)
                        .orElse(DEF_ACCOUNT_LOCKED_DURATION);
                account.setLocked(Instant.now().plus(duration));
            }
            return account;
        }

        if (hasFailedCount != 0) {
            account.setFailedCount(0);
            return account;
        }
        return null;
    }

}
