package com.github.mrzhqiang.rowing.module.i18n;

import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 数据库消息源访问器。
 * <p>
 * 优先从数据库获取国际化消息，如果获取不到，再从系统内置消息源获取，最后才使用默认消息。
 */
public final class DatabaseMessageSourceAccessor extends MessageSourceAccessor {


    public DatabaseMessageSourceAccessor(MessageSource messageSource) {
        super(messageSource);
    }

    public DatabaseMessageSourceAccessor(MessageSource messageSource, Locale defaultLocale) {
        super(messageSource, defaultLocale);
    }
}
