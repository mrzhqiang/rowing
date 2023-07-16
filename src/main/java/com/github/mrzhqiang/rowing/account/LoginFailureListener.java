package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingRepository;
import com.github.mrzhqiang.rowing.util.Authentications;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;

@Component
public class LoginFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private static final int DEF_MAX_LOGIN_FAILED = 5;
    private static final Duration DEF_FIRST_FAILED_DURATION = Duration.ofHours(1);
    private static final Duration DEF_ACCOUNT_LOCKED_DURATION = Duration.ofMinutes(5);

    private static final String MAX_LOGIN_FAILED = "max-login-failed";
    private static final String FIRST_FAILED_DURATION = "first-failed-duration";
    private static final String ACCOUNT_LOCKED_DURATION = "account-locked-duration";

    private final AccountRepository accountRepository;
    private final SettingRepository settingRepository;

    public LoginFailureListener(AccountRepository accountRepository,
                                SettingRepository settingRepository) {
        this.accountRepository = accountRepository;
        this.settingRepository = settingRepository;
    }

    @RunAsSystem
    @Override
    public void onApplicationEvent(@Nonnull AbstractAuthenticationFailureEvent event) {
        Authentication authentication = event.getAuthentication();
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            handleLoginFailed(authentication);
        }
    }

    private void handleLoginFailed(Authentication authentication) {
        Authentications.findUsername(authentication)
                .flatMap(accountRepository::findByUsername)
                .filter(Account::isAccountNonLocked)
                .map(this::computeFailedCount)
                .ifPresent(accountRepository::save);
    }

    private Account computeFailedCount(Account account) {
        Instant firstFailed = account.getFirstFailed();
        Instant now = Instant.now();
        Duration firstFailedDuration = settingRepository.findByName(FIRST_FAILED_DURATION)
                .map(Setting::getContent)
                .map(Duration::parse)
                .orElse(DEF_FIRST_FAILED_DURATION);
        if (firstFailed == null || firstFailed.plus(firstFailedDuration).isBefore(now)) {
            account.setFirstFailed(now);
            account.setFailedCount(1);
            return account;
        }

        int hasFailedCount = account.getFailedCount() + 1;
        account.setFailedCount(hasFailedCount);
        Integer maxLoginFailed = settingRepository.findByName(MAX_LOGIN_FAILED)
                .map(Setting::getContent)
                .map(Integer::parseInt)
                .orElse(DEF_MAX_LOGIN_FAILED);
        if (hasFailedCount >= maxLoginFailed) {
            Duration duration = settingRepository.findByName(ACCOUNT_LOCKED_DURATION)
                    .map(Setting::getContent)
                    .map(Duration::parse)
                    .orElse(DEF_ACCOUNT_LOCKED_DURATION);
            account.setLocked(now.plus(duration));
        }
        return account;
    }
}
