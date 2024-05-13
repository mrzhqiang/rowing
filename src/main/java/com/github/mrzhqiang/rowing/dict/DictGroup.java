package com.github.mrzhqiang.rowing.dict;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.DictType;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 字典组。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class DictGroup extends AuditableEntity {

    private static final long serialVersionUID = -7487255455106320549L;

    /**
     * 名称。
     * <p>
     * 通常用于界面展示。
     */
    @NotBlank
    @Size(max = Domains.DICT_GROUP_NAME_LENGTH)
    @Column(nullable = false, length = Domains.DICT_GROUP_NAME_LENGTH)
    private String name;
    /**
     * 代码。
     * <p>
     * 可以用来查询相关字典项，因此必须保证全局唯一。
     */
    @NotBlank
    @Size(max = Domains.DICT_GROUP_CODE_LENGTH)
    @Column(unique = true, nullable = false, length = Domains.DICT_GROUP_CODE_LENGTH)
    private String code;
    /**
     * 类型。
     */
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_LENGTH)
    private DictType type;
    /**
     * 冻结。
     * <p>
     * 冻结的字典不允许更新，包括初始化同步以及后台编辑时的操作。
     */
    @Enumerated(EnumType.STRING)
    @Column(length = Domains.ENUM_LENGTH)
    private Logic freeze;

    /**
     * 字典项列表。
     * <p>
     * 字典项一般用来提供选择，比如下拉框、多选框等等。
     */
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "group")
    private List<DictItem> items = Lists.newArrayList();

}
