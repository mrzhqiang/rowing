package com.github.mrzhqiang.rowing.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;

import javax.annotation.Nonnull;

/**
 * 国际化消息持有者。
 * <p>
 * 事实上，它在应用初始化时，获取消息源并作为消息源访问器的参数，可以通过获取这个访问器来处理国际化消息。
 */
public final class I18nHolder implements MessageSourceAware {

    private static MessageSourceAccessor accessor;

    public static MessageSourceAccessor getAccessor() {
        return accessor;
    }

    @Override
    public void setMessageSource(@Nonnull MessageSource messageSource) {
        if (accessor == null) {
            accessor = new MessageSourceAccessor(messageSource);
        }
    }
}
