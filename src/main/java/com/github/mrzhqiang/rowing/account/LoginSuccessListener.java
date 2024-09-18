package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.action.Action;
import com.github.mrzhqiang.rowing.domain.ActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;

@Component
@RequiredArgsConstructor
public class LoginSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final AccountService service;

    @Action(ActionType.LOGIN_SUCCESSFUL)
    @RunAsSystem
    @Override
    public void onApplicationEvent(@Nonnull AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        service.handleLoginSuccess(authentication);
    }

}
