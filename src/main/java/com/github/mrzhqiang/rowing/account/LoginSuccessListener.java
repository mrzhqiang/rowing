package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.action.Action;
import com.github.mrzhqiang.rowing.domain.ActionType;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final AccountRepository accountRepository;

    public LoginSuccessListener(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Action(value = "登录成功处理", type = ActionType.LOGIN)
    @RunAsSystem
    @Override
    public void onApplicationEvent(@Nonnull AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        handleLoginSuccess(authentication);
    }

    private void handleLoginSuccess(Authentication authentication) {
        accountRepository.findByUsername(authentication.getName())
                .filter(it -> !it.isAccountNonLocked())
                .map(this::resetNormalAccount)
                .ifPresent(accountRepository::save);
    }

    private Account resetNormalAccount(Account account) {
        account.setFailedCount(0);
        account.setLocked(null);
        return account;
    }
}
