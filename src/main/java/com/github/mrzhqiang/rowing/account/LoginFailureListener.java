package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.config.RowingSecurityProperties;
import com.github.mrzhqiang.rowing.util.Authentications;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;

@Component
public class LoginFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final RowingSecurityProperties rowingSecurityProperties;
    private final AccountRepository accountRepository;

    public LoginFailureListener(RowingSecurityProperties rowingSecurityProperties,
                                AccountRepository accountRepository) {
        this.rowingSecurityProperties = rowingSecurityProperties;
        this.accountRepository = accountRepository;
    }

    @Override
    public void onApplicationEvent(@Nonnull AuthenticationFailureBadCredentialsEvent event) {
        Authentication authentication = event.getAuthentication();
        handleLoginFailed(authentication);
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
        Duration firstFailedDuration = rowingSecurityProperties.getFirstFailedDuration();
        if (firstFailed == null || firstFailed.plus(firstFailedDuration).isBefore(now)) {
            account.setFirstFailed(now);
            account.setFailedCount(1);
            return account;
        }

        int hasFailedCount = account.getFailedCount() + 1;
        account.setFailedCount(hasFailedCount);
        if (hasFailedCount >= rowingSecurityProperties.getMaxLoginFailed()) {
            Duration duration = rowingSecurityProperties.getLockedDuration();
            account.setLocked(now.plus(duration));
        }
        return account;
    }
}
