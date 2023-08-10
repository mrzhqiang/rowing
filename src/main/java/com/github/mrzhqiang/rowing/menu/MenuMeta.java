package com.github.mrzhqiang.rowing.menu;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 菜单元数据。
 */
@Getter
@Setter
@ToString
@Embeddable
public class MenuMeta {

    /**
     * 标题。
     * <p>
     * 标题一般展示在侧边栏以及面包屑中，由于是用户自己创建的内容，通常不支持国际化，也就意味着输入什么内容，就展示什么内容。
     * <p>
     * 未来或许可以借助第三方接口，来实现对应的内容国际化功能，然后再开发自己的内容国际化功能。
     */
    @NotBlank
    @Size(max = 50)
    @Column(length = 50)
    private String title;
    /**
     * 图标。
     * <p>
     * 展示在标题之前的图标，属于内置数据，但不作为枚举实现。
     * <p>
     * 可以在前端硬编码一个图标库列表，支持预览选择，得到相应的图标字符串，传参到后端进行保存。
     * <p>
     * 只需要匹配对应的图标字符串，即显示对应的图标。
     */
    @Size(max = 50)
    @Column(length = 50)
    private String icon;
    /**
     * 不缓存。
     * <p>
     * 如果设置为 true 则页面将不会被缓存，默认为 false。
     */
    private Boolean noCache = false;
    /**
     * 固定。
     * <p>
     * 如果设置为 true 则在标签栏中固定显示，不能移除。
     */
    private Boolean affix;
    /**
     * 面包屑。
     * <p>
     * 如果设置为 false 则在面包屑中隐藏，默认为 true。
     */
    private Boolean breadcrumb = true;
    /**
     * 活动菜单。
     * <p>
     * 如果设置了路径，则在侧边栏高亮匹配到的路径。
     */
    @Size(max = 1024)
    @Column(length = 1024)
    private String activeMenu;

}
