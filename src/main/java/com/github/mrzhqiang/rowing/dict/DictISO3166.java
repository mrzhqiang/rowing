package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * ISO 3166 是一个国际标准，用于定义国家和其子区域的代码。
 * <p>
 * ISO 3166 分为三个部分，分别是 ISO 3166-1、ISO 3166-2 和 ISO 3166-3。
 * <p>
 * ISO 3166-1 是用两个字母、三个字母或三位数字来表示国家或地区的代码，例如 CN、CHN 或 156 都表示中国。
 * <p>
 * ISO 3166-2 是用一个连字符加上一个或多个字母或数字来表示国家或地区的下级行政区划的代码，例如 CN-SC 表示中国四川省。
 * <p>
 * ISO 3166-3 是用四个字母来表示已经被删除或更改名称的国家或地区的代码，例如 CSHH 表示捷克斯洛伐克。
 * <p>
 * 本系统主要采用 ISO 3166-1 标准，有三种代码，分别是：
 * <p>
 * 两位字母代码（alpha2Code），例如 CN 表示中华人民共和国，US 表示美利坚合众国。
 * <p>
 * 三位字母代码（alpha3Code），例如 CHN 表示中华人民共和国，USA 表示美利坚合众国。
 * <p>
 * 三位数字代码（numericCode），例如 156 表示中华人民共和国，840 表示美利坚合众国。
 *
 * @see <a href="https://zh.wikipedia.org/wiki/ISO_3166-1#%E6%AD%A3%E5%BC%8F%E4%BB%A3%E7%A2%BC%E5%88%97%E8%A1%A8">ISO_3166-1#正式代碼列表</a>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "dict_iso_3166")
public class DictISO3166 extends AuditableEntity {

    private static final long serialVersionUID = -8561271023370373377L;

    /**
     * 英文短名称。
     */
    @Column(length = 100)
    private String name;
    /**
     * 中文名称。
     */
    @Column(length = 100)
    private String cnName;
    /**
     * 两位字母代码。
     */
    @Column(unique = true, nullable = false, length = 2)
    private String alpha2Code;
    /**
     * 三位字母代码。
     */
    @Column(unique = true, nullable = false, length = 3)
    private String alpha3Code;
    /**
     * 三位数字代码。
     */
    @Column(unique = true, nullable = false, length = 3)
    private String numericCode;

}
