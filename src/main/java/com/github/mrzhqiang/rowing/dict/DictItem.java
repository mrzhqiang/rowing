package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 字典项。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"group_code", "value"})})
public class DictItem extends AuditableEntity {

    private static final long serialVersionUID = -8038113027410128044L;

    /**
     * 标签。
     * <p>
     * 标签是字典项的文字说明。
     * <p>
     * 对于内置类型，一般以初始化时的系统本地语言为主，通过国际化语言文件转换而成。
     * <p>
     * 对于自定义类型，则以管理输入的内容为主。
     */
    @NotBlank
    @Size(max = Domains.DICT_ITEM_LABEL_LENGTH)
    @Column(nullable = false, length = Domains.DICT_ITEM_LABEL_LENGTH)
    private String label;
    /**
     * 值。
     * <p>
     * 字典项所代表的值。
     */
    @NotBlank
    @Size(max = Domains.DICT_ITEM_VALUE_LENGTH)
    @Column(nullable = false, length = Domains.DICT_ITEM_VALUE_LENGTH)
    private String value;

    /**
     * 字典项所属字典组。
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "group_code", referencedColumnName = "code",
            nullable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private DictGroup group;

}
