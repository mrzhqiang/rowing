package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.action.Action;
import com.github.mrzhqiang.rowing.domain.ActionType;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

@Service
public class LogoutSuccessListener implements ApplicationListener<LogoutSuccessEvent> {

    @Action(value = "注销成功处理", type = ActionType.LOGOUT)
    @Override
    public void onApplicationEvent(@Nonnull LogoutSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        handleLogoutSuccess(authentication);
    }

    private void handleLogoutSuccess(Authentication authentication) {
        // 目前没有处理逻辑，等待丰富
    }

}
