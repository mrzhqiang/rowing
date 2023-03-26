package com.github.mrzhqiang.rowing.i18n;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Locale;

/**
 * 国际化语言标签。
 * <p>
 * 语言标签通常由 `ISO 639-1` 规范中定义的语言代码加上 `ISO 3166` 规范中定义的国家/地区代码组成，比如简体中文就是 `zh_CN` 语言标签。
 * <p>
 * 虽然数据字典包含以上两个规范的数据，但由于不同的 JVM 实现，可能存在不同的本地化数据，因此这个实体数据默认情况下来自 {@link Locale#getAvailableLocales()} 方法。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class I18nLang extends AuditableEntity {

    /**
     * 代码。
     * <p>
     * 通常是语言标签代码。
     */
    @Column(unique = true, nullable = false, length = 20)
    private String code;
    /**
     * 名称。
     * <p>
     * 通常是语言标签代码对应的名称，默认情况下和代码一样，也可以编辑为用户母语对应的名称。
     */
    @Column(length = 40)
    private String name;

    @ToString.Exclude
    @OneToMany(mappedBy = "lang")
    private List<I18nGroup> groupList;
}
