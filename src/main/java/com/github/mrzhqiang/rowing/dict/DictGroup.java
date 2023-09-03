package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.DictType;
import com.github.mrzhqiang.rowing.domain.Domains;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    @Size(max = Domains.DICT_NAME_LENGTH)
    @Column(nullable = false, length = Domains.DICT_NAME_LENGTH)
    private String name;
    /**
     * 代码。
     * <p>
     * 可以用来查询相关字典项，因此必须保证全局唯一。
     */
    @NotBlank
    @Size(max = Domains.DICT_CODE_LENGTH)
    @Column(unique = true, nullable = false, length = Domains.DICT_CODE_LENGTH)
    private String code;
    /**
     * 类型。
     */
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_NAME_LENGTH)
    private DictType type;
    /**
     * 冻结。
     * <p>
     * 冻结的字典不允许更新，包括初始化同步以及后台编辑时的操作。
     */
    @Enumerated(EnumType.STRING)
    @Column(length = Domains.ENUM_NAME_LENGTH)
    private Logic freeze;

    /**
     * 字典项列表。
     * <p>
     * 字典项一般用来提供选择，比如下拉框、多选框等等。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "group", orphanRemoval = true)
    private List<DictItem> items;

}
