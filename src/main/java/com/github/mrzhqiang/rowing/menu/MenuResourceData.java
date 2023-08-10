package com.github.mrzhqiang.rowing.menu;

import lombok.Data;

/**
 * 菜单资源数据。
 */
@Data
public class MenuResourceData {

    /**
     * 资源 ID
     */
    private Long id;
    /**
     * 资源名称。
     */
    private String name;
    /**
     * 资源权限。
     */
    private String authority;
    /**
     * 资源排序。
     */
    private Integer ordered;

}
