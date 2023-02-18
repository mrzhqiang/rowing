package com.github.mrzhqiang.rowing.modules.account;

import com.github.mrzhqiang.helper.Joiners;
import com.github.mrzhqiang.rowing.config.RowingSecurityProperties;
import com.github.mrzhqiang.rowing.util.DateTimes;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final RowingSecurityProperties rowingSecurityProperties;
    private final AccountRepository accountRepository;
    private final MessageSourceAccessor sourceAccessor;

    public LoginFailureHandler(RowingSecurityProperties rowingSecurityProperties,
                               AccountRepository accountRepository,
                               MessageSource messageSource) {
        this.rowingSecurityProperties = rowingSecurityProperties;
        this.accountRepository = accountRepository;
        this.sourceAccessor = new MessageSourceAccessor(messageSource);
        // 自定义失败处理器，我们需要固定失败的重定向地址
        setDefaultFailureUrl(rowingSecurityProperties.getLoginFailedPath());
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        String username = request.getParameter(AccountService.USERNAME_KEY);
        AuthenticationException rawException = exception;
        exception = Optional.ofNullable(username)
                .flatMap(accountRepository::findByUsername)
                .map(it -> handleException(it, rawException))
                .orElse(rawException);
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
        Integer maxLoginFailed = rowingSecurityProperties.getMaxLoginFailed();
        String rawMessage = exception.getMessage();
        String message = sourceAccessor.getMessage("AccountAuthenticationFailureHandler.BadCredentialsException",
                new Object[]{currentFailedCount, maxLoginFailed});
        return new BadCredentialsException(Joiners.DASH.join(rawMessage, message), exception);
    }

    private LockedException parseLocked(Account account, LockedException exception) {
        Instant locked = account.getLocked();
        String lockedDuration = DateTimes.haveTime(Instant.now(), locked);
        String rawMessage = exception.getMessage();
        String message = sourceAccessor.getMessage("AccountAuthenticationFailureHandler.LockedException",
                new Object[]{lockedDuration});
        return new LockedException(Joiners.DASH.join(rawMessage, message), exception);
    }
}
