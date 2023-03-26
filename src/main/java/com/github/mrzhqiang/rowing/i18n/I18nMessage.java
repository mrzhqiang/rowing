package com.github.mrzhqiang.rowing.i18n;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * 国际化消息。
 * <p>
 * 这个实体是自定义的国际化消息，与系统内置的国际化消息不一样，自定义消息主要是提供数据给前端使用。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class I18nMessage extends AuditableEntity {

    /**
     * 所属分组。
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "group_code", referencedColumnName = "code", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private I18nGroup group;
    /**
     * 消息键名。
     */
    @Column(nullable = false, length = 100)
    private String key;
    /**
     * 消息内容。
     */
    @Column(length = 200)
    private String content;

}
