package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.google.common.collect.Lists;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 行政区划代码。
 * <p>
 * 行政区划代码是用于行政区划上的识别码，同时是一项由国家标准机构——中国国家标准化管理委员会通过并公开发布的国家标准，
 * 国标号为 GB/T 2260，并加上后缀发布年份，如 GB/T 2260-2007 是当前使用的版本。
 * <p>
 * 行政区划代码分为三层结构，共六位代码。
 * <p>
 * 第一层（第一、二位代码）：一级行政区，包含直辖市、省（自治区）、特别行政区以及台湾省。
 * <p>
 * 第二层（第三、四位代码）：
 * <p>
 * 1. 省（自治区）下辖的地级行政区：
 * 01-20、51-70 表示地级市；
 * 21-50 表示地区、自治州、盟。
 * <p>
 * 2. 直辖市下辖的县级行政区：
 * 01 表示市辖区的汇总码；
 * 02 表示县、自治县的汇总码；
 * 03 表示县级市的汇总码（目前不再使用）。
 * <p>
 * 3. 省（自治区）直辖县级行政区：
 * 90 表示县级行政区的汇总码。
 * <p>
 * 第三层（第五、六位代码）：
 * <p>
 * 1. 地级市下辖的县级行政区：
 * 01-20、51-80 表示市辖区、特区、工矿区（其中 01 表示市辖区的汇总码，因为历史原因 520201 不是汇总码）；
 * 21-50 表示县、自治县、旗、自治旗；
 * 81-99 表示地级市代管的县级市。
 * <p>
 * 2. 直辖市下辖的县级行政区：
 * 01-20、51-80 表示市辖区；
 * 21-50 表示县、自治县；
 * 81-99 表示县级市（目前不再使用）。
 * <p>
 * 3. 地区（自治州、盟）下辖的县级行政区，省（自治区）直辖县级行政区：
 * 01-20 表示县级市；
 * 21-80 表示县、自治县、旗、特区、林区、工农区、县级镇、县级管理区。
 *
 * @see <a href="https://zh.wikipedia.org/wiki/%E4%B8%AD%E5%8D%8E%E4%BA%BA%E6%B0%91%E5%85%B1%E5%92%8C%E5%9B%BD%E8%A1%8C%E6%94%BF%E5%8C%BA%E5%88%92%E4%BB%A3%E7%A0%81">中华人民共和国行政区划代码</a>
 * @see <a href="https://www.mca.gov.cn/mzsj/xzqh/2022/202201xzqh.html">民政部行政区划代码2022年</a>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "dict_gbt_2260")
public class DictGBT2260 extends AuditableEntity {

    private static final long serialVersionUID = 8130595452091921378L;

    /**
     * 上级单位。
     */
    @ManyToOne
    @JoinColumn(name = "parent_code", referencedColumnName = "code",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private DictGBT2260 parent;
    /**
     * 下级单位列表。
     */
    @ToString.Exclude
    @OrderBy("code ASC, created DESC")
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<DictGBT2260> children = Lists.newArrayList();

    /**
     * 单位名称。
     */
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;
    /**
     * 行政区划代码。
     */
    @NotBlank
    @Size(max = 6)
    @Column(nullable = false, length = 6)
    private String code;
    /**
     * 行政区级别。
     * <p>
     * 省级行政区：{@link Dicts#FIRST_LEVEL}。
     * <p>
     * 地级行政区：{@link Dicts#SECOND_LEVEL}。
     * <p>
     * 县级行政区：{@link Dicts#THIRD_LEVEL}。
     */
    @NotBlank
    @Size(max = 1)
    @Column(nullable = false, length = 1)
    private String level;
    /**
     * 是否为汇总码。
     */
    @Column(columnDefinition = "bit(1) default 0")
    private Boolean summary = false;

}
