package com.github.mrzhqiang.rowing.i18n;

import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;

/**
 * 国际化持有者。
 * <p>
 * 此类持有一个消息源访问器，通过消息源访问器，可以获取当前环境下的语言标签所对应的国际化消息。
 * <p>
 * 如果在请求的 Header 中发现 Accept-Language 存在语言标签列表，则 ServletRequest 的 getLocale 方法
 * 会获取到首选语言标签，从而在当前线程上下文中，将默认语言标签切换为指定语言标签，随后系统获取的消息内容都将基于此语言标签。
 */
@Component
@Order(Integer.MIN_VALUE)
public final class I18nHolder implements MessageSourceAware {

    @Getter
    private static MessageSourceAccessor accessor;

    @Override
    public void setMessageSource(@Nonnull MessageSource messageSource) {
        if (accessor == null) {
            accessor = new MessageSourceAccessor(messageSource);
        }
    }

}
