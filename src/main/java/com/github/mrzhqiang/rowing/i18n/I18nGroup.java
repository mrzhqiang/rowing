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
import javax.persistence.OneToMany;
import java.util.List;

/**
 * 国际化分组。
 * <p>
 * 分组主要是为了确定国际化消息的唯一性。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class I18nGroup extends AuditableEntity {

    /**
     * 语言标签代码。
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "lang_code", referencedColumnName = "code", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private I18nLang lang;
    /**
     * 分组成员。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "owner")
    private List<I18nGroup> members;
    /**
     * 分组所属。
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "parent_code", referencedColumnName = "code", nullable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private I18nGroup owner;
    /**
     * 分组代码。
     */
    @Column(nullable = false, length = 100)
    private String code;
    /**
     * 分组名称。
     */
    @Column(length = 200)
    private String name;

    /**
     * 分组内的消息。
     */
    @OneToMany(mappedBy = "group")
    private List<I18nMessage> messages;

}
