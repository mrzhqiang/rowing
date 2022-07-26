package com.github.mrzhqiang.rowing.core.system;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * 字典分组。
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
    @Column(unique = true, nullable = false)
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
     * 字典项列表。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "group")
    private List<DataDictItem> items;

    /**
     * 字典分组类型。
     */
    public enum Type {
        /**
         * DB 字典。
         * <p>
         * 默认情况下的字典项。
         */
        DB,
        /**
         * API 接口字典。
         * <p>
         * 通常使用 JSON 解析为对应的字典项。
         */
        API,
        /**
         * Excel 文件字典。
         * <p>
         * 通常是单页 Sheet 内容，列名对应字典项字段名称。
         */
        EXCEL,
        /**
         * 纯文本字典。
         * <p>
         * 一般以英文逗号(,)分割，对应字典项中的字段。
         */
        TXT
    }
}
