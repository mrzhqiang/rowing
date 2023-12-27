package com.github.mrzhqiang.rowing.dict.iso;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * ISO 639 是关于语言代码的标准。
 * <p>
 * 它分为五个部分，分别是 ISO 639-1、ISO 639-2、ISO 639-3、ISO 639-4 和 ISO 639-5。
 * <p>
 * ISO 639-1 是用两个字母来表示主要语言的代码，例如 zh 表示中文，en 表示英文。
 * <p>
 * ISO 639-2 是用三个字母来表示所有语言的代码，其中有些语言有两种不同的代码，一种是终端用户使用的（T），一种是文献学使用的（B），例如 chi 或 zho 都表示中文。
 * <p>
 * ISO 639-3 是用三个字母来表示所有语言变体的代码，包括方言、历史语言等，例如 cmn 表示普通话，yue 表示粤语。
 * <p>
 * ISO 639-4 是关于语言代码标准的通用原则和指南。
 * <p>
 * ISO 639-5 是用三个字母来表示语言族和群的代码，例如 zhx 表示中文语族。
 * <p>
 * 本系统主要采用 ISO 639-1 标准。
 *
 * @see <a href="https://zh.wikipedia.org/wiki/ISO_639-1%E4%BB%A3%E7%A0%81%E8%A1%A8">ISO_639-1代码表</a>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "dict_iso_639")
public class DictISO639 extends AuditableEntity {

    private static final long serialVersionUID = 2247815389239108380L;

    /**
     * 语系。
     * <p>
     * 为了避免循环嵌套，语系不再设计为枚举、字典或者其他实体，仅作为字符列。
     *
     * @see <a href="https://zh.wikipedia.org/wiki/%E8%AF%AD%E8%A8%80%E7%B3%BB%E5%B1%9E%E5%88%86%E7%B1%BB">语言系属分类</a>
     */
    @Size(max = 50)
    @Column(length = 50)
    private String family;
    /**
     * 中文名称。
     */
    @Size(max = 50)
    @Column(length = 50)
    private String cnName;
    /**
     * 该语言自称。
     * <p>
     * 比如：
     * <p>
     * 朝鲜语：한국어／韓國語
     * 朝鲜语：朝鮮말／조선말
     */
    @Size(max = 100)
    @Column(length = 100)
    private String selfName;
    /**
     * ISO 语言名称。
     * <p>
     * 比如：
     * <p>
     * Church Slavic, Old Slavonic, Church Slavonic, Old Bulgarian, Old Church Slavonic
     */
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;
    /**
     * ISO 语言代码。
     * <p>
     * 基于 ISO 639-1 标准，只有两位代码。
     */
    @NotBlank
    @Size(max = 2)
    @Column(nullable = false, unique = true, length = 2)
    private String code;
    /**
     * 注释。
     */
    @Column(columnDefinition = Domains.TEXT_COLUMN_TYPE)
    private String notes;

}
