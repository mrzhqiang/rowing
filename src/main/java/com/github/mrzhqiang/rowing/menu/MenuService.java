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
     * 查询菜单路由。
     *
     * @return 菜单数据列表。
     */
    List<MenuRoute> findRoutes();

}
