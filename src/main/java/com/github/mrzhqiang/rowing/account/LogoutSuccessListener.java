package com.github.mrzhqiang.rowing.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogoutSuccessListener implements ApplicationListener<LogoutSuccessEvent> {

    @Override
    public void onApplicationEvent(@Nonnull LogoutSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        handleLogoutSuccess(authentication);
    }

    private void handleLogoutSuccess(Authentication authentication) {
        log.info("logout success: {}", authentication);
    }

}
