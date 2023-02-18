package com.github.mrzhqiang.rowing.modules.dict;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 字典项。
 * <p>
 * 字典项属于字典组的成员，通常用字典组代码即可获得，因此不同字典组所包含的字典项可以相同，但相同字典组下的字典项不能重复。
 * <p>
 * 对于内置数据字典，由枚举的性质保证字典项不重复。
 * <p>
 * 对于自定义数据字典，由前端校验控制字典项不重复。
 * <p>
 * 因此，其他可能导致数据重复的情况，需要手动处理。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class DictItem extends AuditableEntity {

    /**
     * 图标。
     * <p>
     * 允许为空，说明字典项不存在前置图标，则仅展示标签内容。
     * <p>
     * 图标一般以图标库引用名称为主，通常直接使用即可。
     */
    private String icon;
    /**
     * 样式。
     * <p>
     * 允许为空，说明字典项不存在前端样式，则仅展示标签内容。
     * <p>
     * 样式一般以 css 选择器为主，通常直接使用即可。
     */
    private String style;
    /**
     * 标签。
     * <p>
     * 标签是字典项的文字说明。
     * <p>
     * 对于内置类型，一般以初始化时的系统本地语言为主，通过国际化语言文件转换而成。
     * <p>
     * 对于自定义类型，则以管理员输入的内容为主。
     */
    @Column(nullable = false)
    private String label;
    /**
     * 值。
     * <p>
     * 字典项所代表的值。
     */
    @Column(nullable = false)
    private String value;

    /**
     * 字典项所属字典组。
     */
    @ManyToOne(optional = false)
    private DictGroup group;

}
