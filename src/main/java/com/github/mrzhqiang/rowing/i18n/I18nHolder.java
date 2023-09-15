package com.github.mrzhqiang.rowing.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

/**
 * 国际化持有者。
 * <p>
 * 此类持有一个消息源访问器，通过消息源访问器，可以获取当前环境下的语言标签所对应的国际化消息。
 */
@Component
@Order(Integer.MIN_VALUE)
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
