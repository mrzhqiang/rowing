package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.action.Action;
import com.github.mrzhqiang.rowing.domain.ActionType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationFailureLockedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;

/**
 * 登录失败监听器。
 * <p>
 * 监听抽象的认证失败事件，通过不同的认证失败事件，进行不同的处理逻辑。
 */
@Component
@RequiredArgsConstructor
public class LoginFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    private final AccountService service;

    /**
     * 注意：通常是先发出登录失败的事件，再执行登录失败的回调。
     */
    @Action(ActionType.LOGIN_FAILURE)
    @RunAsSystem
    @Override
    public void onApplicationEvent(@Nonnull AbstractAuthenticationFailureEvent event) {
        Authentication authentication = event.getAuthentication();
        // 如果认证失败的事件是 密码错误 or 账户被锁定 才需要处理失败的情况
        if (event instanceof AuthenticationFailureBadCredentialsEvent
                || event instanceof AuthenticationFailureLockedEvent) {
            service.handleLoginFailed(authentication);
        }
    }

}
