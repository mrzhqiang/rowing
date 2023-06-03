package com.github.mrzhqiang.rowing.dict;

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
 * 字典项。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class DictItem extends AuditableEntity {

    /**
     * 标签。
     * <p>
     * 标签是字典项的文字说明。
     * <p>
     * 对于内置类型，一般以初始化时的系统本地语言为主，通过国际化语言文件转换而成。
     * <p>
     * 对于自定义类型，则以管理员输入的内容为主。
     */
    @Column(nullable = false, length = 100)
    private String label;
    /**
     * 值。
     * <p>
     * 字典项所代表的值。
     */
    @Column(nullable = false, length = 50)
    private String value;

    /**
     * 字典项所属字典组。
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "group_code", referencedColumnName = "code",
            nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private DictGroup group;

}
