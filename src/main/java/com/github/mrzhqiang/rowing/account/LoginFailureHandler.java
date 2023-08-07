package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.helper.Joiners;
import com.github.mrzhqiang.helper.time.DateTimes;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

/**
 * 登录失败处理器。
 * <p>
 * 主要对登录失败的各种异常进行处理，比如密码错误的异常，会记录一次失败，如果失败次数超过阈值，则锁定一段时间账户，避免暴力破解密码。
 */
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final AccountService accountService;
    private final SettingService settingService;

    public LoginFailureHandler(AccountService accountService,
                               SettingService settingService) {
        this.accountService = accountService;
        this.settingService = settingService;
    }

    /**
     * 注意：通常是先发出登录失败的事件，再执行登录失败的回调。
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String username = request.getParameter(AccountService.USERNAME_KEY);
        AuthenticationException authenticationException = Optional.ofNullable(username)
                .map(accountService::loadUserByUsername)
                .map(it -> handleException(it, exception))
                .orElse(exception);
        response.sendError(HttpStatus.UNAUTHORIZED.value(), authenticationException.getMessage());
    }

    private AuthenticationException handleException(Account account, AuthenticationException exception) {
        if (exception instanceof BadCredentialsException) {
            // 如果是用户名或密码错误，那么我们返回更多的细节，比如：已尝试多少次，还可以几次机会
            BadCredentialsException rawException = (BadCredentialsException) exception;
            return parseBadCredentials(account, rawException);
        }
        if (exception instanceof LockedException) {
            // 如果是账户被锁定错误，那么我们提示锁定剩余时长
            LockedException rawException = (LockedException) exception;
            return parseLocked(account, rawException);
        }
        return exception;
    }

    private BadCredentialsException parseBadCredentials(Account account, BadCredentialsException exception) {
        int currentFailedCount = account.getFailedCount();
        Integer maxLoginFailed = settingService.findByName(SettingService.MAX_LOGIN_FAILED)
                .map(Setting::getContent)
                .map(Integer::parseInt)
                .orElse(SettingService.DEF_MAX_LOGIN_FAILED);
        String rawMessage = exception.getMessage();
        String message = I18nHolder.getAccessor().getMessage(
                "LoginFailureHandler.BadCredentialsException", new Object[]{currentFailedCount, maxLoginFailed});
        return new BadCredentialsException(Joiners.DASH.join(rawMessage, message), exception);
    }

    private LockedException parseLocked(Account account, LockedException exception) {
        Instant locked = account.getLocked();
        String lockedDuration = DateTimes.haveTime(Instant.now(), locked);
        String rawMessage = exception.getMessage();
        String message = I18nHolder.getAccessor().getMessage(
                "LoginFailureHandler.LockedException", new Object[]{lockedDuration});
        return new LockedException(Joiners.DASH.join(rawMessage, message), exception);
    }

}
