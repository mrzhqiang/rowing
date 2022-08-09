package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.account.AccountRepository;
import com.github.mrzhqiang.rowing.account.AccountService;
import com.github.mrzhqiang.rowing.util.DateTimes;
import com.github.mrzhqiang.rowing.util.Joiners;
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

    private final SecurityProperties securityProperties;
    private final AccountRepository accountRepository;
    private final MessageSourceAccessor messageSourceAccessor;

    public LoginFailureHandler(SecurityProperties securityProperties,
                               AccountRepository accountRepository,
                               MessageSource messageSource) {
        this.securityProperties = securityProperties;
        this.accountRepository = accountRepository;
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
        // 自定义失败处理器，我们需要固定失败的重定向地址
        setDefaultFailureUrl(securityProperties.getLoginFailedPath());
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
        Integer maxLoginFailed = securityProperties.getMaxLoginFailed();
        String rawMessage = exception.getMessage();
        String message = messageSourceAccessor.getMessage("AccountAuthenticationFailureHandler.BadCredentialsException",
                new Object[]{currentFailedCount, maxLoginFailed});
        return new BadCredentialsException(Joiners.MESSAGE.join(rawMessage, message), exception);
    }

    private LockedException parseLocked(Account account, LockedException exception) {
        Instant locked = account.getLocked();
        String lockedDuration = DateTimes.haveTime(Instant.now(), locked);
        String rawMessage = exception.getMessage();
        String message = messageSourceAccessor.getMessage("AccountAuthenticationFailureHandler.LockedException",
                new Object[]{lockedDuration});
        return new LockedException(Joiners.MESSAGE.join(rawMessage, message), exception);
    }
}
