package com.github.mrzhqiang.rowing.system.data;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * 字典组。
 * <p>
 * 分组是为了给字典分类，支持不同类型存在相似的字典项，而不引发冲突。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class DataDictGroup extends AuditableEntity {

    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String code;
    @Enumerated(EnumType.STRING)
    private Type type;

    /**
     * 组内的字典项列表。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<DataDictItem> items;

    /**
     * 字典组类型。
     */
    public enum Type {
        /**
         * 内置字典组。
         * <p>
         * 通过系统资源中的 Excel 文件加载的内置数据字典。
         */
        DEFAULT,
        /**
         * 自定义字典组。
         * <p>
         * 通过系统手动添加或修改，则被设置为此类型。
         */
        CUSTOMIZE,
    }
}
