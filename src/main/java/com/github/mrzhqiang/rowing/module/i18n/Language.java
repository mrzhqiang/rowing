package com.github.mrzhqiang.rowing.module.i18n;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.LanguageCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 国际化多语言。
 * <p>
 * 使用多语言代码定义这一条消息的不同语言类型。
 * <p>
 * 使用消息键定义这一条消息的全局唯一性。
 * <p>
 * 使用消息内容定义消息的不同语言内容。
 * <p>
 * 注意：这里只是利用数据库持久化多语言，实际上在系统启动时会自动热加载多语言数据到缓存，保证国际化性能。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Language extends AuditableEntity {

    /**
     * 语言代码。
     * <p>
     * 表示不同语言的类型。
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LanguageCode code;
    /**
     * 消息键。
     * <p>
     * 表示这条消息的全局唯一性，但在不同语言类型下，它被视为消息组。
     */
    @Column(nullable = false)
    private String messageKey;
    /**
     * 消息内容。
     * <p>
     * 表示这条消息在不同语言类型下的对应内容。
     */
    @Column(nullable = false)
    private String messageContent;
}
