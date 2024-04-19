package com.github.mrzhqiang.rowing.config;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.ResourceUtils;

import java.util.List;

/**
 * 菜单属性。
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("rowing.menu")
public class MenuProperties {

    /**
     * 默认的菜单路由文件路径。
     */
    private static final String DEF_MENU_ROUTES_PATH = ResourceUtils.CLASSPATH_URL_PREFIX + "data/menu-routes.json";

    /**
     * 菜单 json 文件路径列表。
     */
    private List<String> jsonPaths = ImmutableList.of(DEF_MENU_ROUTES_PATH);

}
