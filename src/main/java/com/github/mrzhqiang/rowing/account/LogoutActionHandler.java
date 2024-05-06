package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.action.Action;
import com.github.mrzhqiang.rowing.domain.ActionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 注销操作处理器。
 */
@Slf4j
@Component
public class LogoutActionHandler implements LogoutHandler {

    @Action(ActionType.LOGOUT)
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("logout action handler: {}", authentication);
    }

}
