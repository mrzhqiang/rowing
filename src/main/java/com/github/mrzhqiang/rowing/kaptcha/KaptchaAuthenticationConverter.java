package com.github.mrzhqiang.rowing.kaptcha;

import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 验证码认证转换器。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KaptchaAuthenticationConverter implements AuthenticationConverter {

    private final KaptchaProperties properties;

    @Override
    public Authentication convert(HttpServletRequest request) {
        if (!properties.getEnabled()) {
            if (log.isDebugEnabled()) {
                log.debug("kaptcha was disabled for {}", request.getRequestURI());
            }
            return null;
        }

        String code = request.getParameter(properties.getParameter());
        if (!StringUtils.hasText(code)) {
            String emptyTips = I18nHolder.getAccessor().getMessage(
                    "KaptchaAuthenticationConverter.code.empty", KaptchaProperties.DEF_EMPTY_TIPS);
            throw new AuthenticationCredentialsNotFoundException(emptyTips);
        }

        HttpSession session = request.getSession();
        String sessionCode = String.valueOf(session.getAttribute(KaptchaProperties.KEY_SESSION_CODE));
        if (!StringUtils.hasText(sessionCode) || !Objects.equals(code, sessionCode)) {
            String invalidTips = I18nHolder.getAccessor().getMessage(
                    "KaptchaAuthenticationConverter.code.invalid", KaptchaProperties.DEF_INVALID_TIPS);
            throw new BadCredentialsException(invalidTips);
        }

        Object date = session.getAttribute(KaptchaProperties.KEY_SESSION_DATE);
        if (date instanceof LocalDateTime) {
            LocalDateTime timeout = ((LocalDateTime) date).plus(properties.getTimeout());
            if (LocalDateTime.now().isAfter(timeout)) {
                String timeoutTips = I18nHolder.getAccessor().getMessage(
                        "KaptchaAuthenticationConverter.code.expired", KaptchaProperties.DEF_TIMEOUT_TIPS);
                throw new CredentialsExpiredException(timeoutTips);
            }
        }
        return null;
    }

}
