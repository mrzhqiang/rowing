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

    /**
     * 分组名称。
     * <p>
     * 即 Label 元素的文字内容。
     */
    @Column(nullable = false)
    private String name;
    /**
     * 分组代码。
     * <p>
     * 通过代码可以获得具体的字典项，填充到对应 Radio 或 Select 元素中。
     */
    @Column(unique = true, nullable = false)
    private String code;
    /**
     * 分组类型。
     * <p>
     * 允许为 null 值，表示内置类型，数据来自于类路径的 Excel 文件。
     */
    @Enumerated(EnumType.STRING)
    private Type type;
    /**
     * 字典来源。
     * <p>
     * 当分组类型为数据库时，来源一般为 null 值。
     * <p>
     * 其他类型则是 URL 地址，一般是 http api、file: 或 classpath: 链接。
     */
    private String source;
    /**
     * 序列号。
     * <p>
     * 以 yyyyMMddHHmmss 格式作为序列号。
     */
    private String serialNo;

    /**
     * 字典项列表。
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
