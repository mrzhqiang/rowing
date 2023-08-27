package com.github.mrzhqiang.rowing.menu;

import java.util.List;

/**
 * 菜单服务。
 * <p>
 */
public interface MenuService {

    /**
     * 路径分隔符。
     */
    String PATH_SEPARATOR = "/";

    /**
     * 初始化。
     */
    void init();

    /**
     * 列出所有顶级菜单。
     * <p>
     * 顶级菜单下包含子级菜单。
     *
     * @return 顶级菜单列表。
     */
    List<MenuData> listRoot();
}
