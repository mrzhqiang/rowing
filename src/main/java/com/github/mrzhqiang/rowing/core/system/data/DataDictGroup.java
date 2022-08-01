package com.github.mrzhqiang.rowing.core.system.data;

import com.github.mrzhqiang.rowing.core.domain.AuditableEntity;
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
 * 分组是为了给字典分类，因为可能存在多个字典项相同，但部分字典项不同的情况，此时如果混杂在一起，不利于筛选。
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
     * 字典组来源。
     * <p>
     * 1. 内置的默认类型，来源是 classpath 上的内置数据字典 Excel 文件绝对路径。
     * <p>
     * 2. 系统管理的 DB 类型，来源是操作用户的用户名。
     * <p>
     * 3. 第三方 API 类型，来源是对应的 URL 地址。
     * <p>
     * 来源仅作为数据参考，不具备使用价值。
     */
    private String source;
    /**
     * 序列号。
     * <p>
     * 以 yyyyMMddHHmmss 格式作为序列号。
     */
    private String serialNo;

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
         * DB 字典组。
         * <p>
         * 通过系统手动添加或修改，则被设置为此类型。
         */
        DB,
        /**
         * API 字典组。
         * <p>
         * 从第三方 API 接口获取的数据字典，则被设置为此类型。
         */
        API,
    }
}
