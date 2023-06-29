package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.helper.Joiners;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingRepository;
import com.github.mrzhqiang.rowing.util.DateTimes;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final int DEF_MAX_LOGIN_FAILED = 5;
    private static final Duration DEF_FIRST_FAILED_DURATION = Duration.ofHours(1);
    private static final Duration DEF_LOCKED_DURATION = Duration.ofMinutes(5);

    private static final String MAX_LOGIN_FAILED = "max-login-failed";
    private static final String FIRST_FAILED_DURATION = "first-failed-duration";
    private static final String ACCOUNT_LOCKED_DURATION = "account-locked-duration";

    private final AccountRepository accountRepository;
    private final SettingRepository settingRepository;

    public LoginFailureHandler(AccountRepository accountRepository,
                               SettingRepository settingRepository) {
        this.accountRepository = accountRepository;
        this.settingRepository = settingRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter(AccountService.USERNAME_KEY);
        AuthenticationException rawException = exception;
        exception = Optional.ofNullable(username)
                .flatMap(accountRepository::findByUsername)
                .map(it -> handleException(it, rawException))
                .orElse(rawException);
        // todo 返回登录失败提示
        super.onAuthenticationFailure(request, response, exception);
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
        Integer maxLoginFailed = settingRepository.findByName(MAX_LOGIN_FAILED)
                .map(Setting::getContent)
                .map(Integer::parseInt)
                .orElse(DEF_MAX_LOGIN_FAILED);
        String rawMessage = exception.getMessage();
        String message = I18nHolder.getAccessor().getMessage("LoginFailureHandler.BadCredentialsException",
                new Object[]{currentFailedCount, maxLoginFailed});
        return new BadCredentialsException(Joiners.DASH.join(rawMessage, message), exception);
    }

    private LockedException parseLocked(Account account, LockedException exception) {
        Instant locked = account.getLocked();
        String lockedDuration = DateTimes.haveTime(Instant.now(), locked);
        String rawMessage = exception.getMessage();
        String message = I18nHolder.getAccessor().getMessage("LoginFailureHandler.LockedException",
                new Object[]{lockedDuration});
        return new LockedException(Joiners.DASH.join(rawMessage, message), exception);
    }
}
