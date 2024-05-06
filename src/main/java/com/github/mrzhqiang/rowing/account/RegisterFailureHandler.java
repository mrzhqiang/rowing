package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注册失败处理器。
 * <p>
 * 主要对注册失败的异常进行处理，比如验证码为空、验证码匹配失败等，给前端返回比较友好的错误消息。
 */
@Component
public class RegisterFailureHandler extends AuthenticationEntryPointFailureHandler {

    public RegisterFailureHandler() {
        super(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
    }

    @RunAsSystem
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String message = null;
        if (exception instanceof AuthenticationCredentialsNotFoundException) {
            message = I18nHolder.getAccessor().getMessage(
                    "RegisterFailureHandler.AuthenticationCredentialsNotFoundException", exception.getMessage());
        } else if (exception instanceof BadCredentialsException) {
            message = I18nHolder.getAccessor().getMessage(
                    "RegisterFailureHandler.BadCredentialsException", exception.getMessage());
        } else if (exception instanceof CredentialsExpiredException) {
            message = I18nHolder.getAccessor().getMessage(
                    "RegisterFailureHandler.CredentialsExpiredException", exception.getMessage());
        }
        if (message != null) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), message);
            return;
        }
        super.onAuthenticationFailure(request, response, exception);
    }

}
