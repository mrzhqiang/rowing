package com.github.mrzhqiang.rowing.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@ConfigurationProperties("rowing.security")
public class SecurityProperties {

    /**
     * 默认的登录路径。
     */
    private static final String DEF_LOGIN_PATH = "/login";
    /**
     * 默认的登出路径。
     */
    private static final String DEF_LOGOUT_PATH = "/logout";
    /**
     * 默认的注册路径。
     */
    private static final String DEF_REGISTER_PATH = "/register";
    /**
     * 默认的错误路径。
     */
    private static final String DEF_ERROR_PATH = "/error";
    /**
     * 默认的详细错误路径。
     */
    private static final String DEF_ERROR_DETAIL_PATH = "/error/**";

    /**
     * 登录路径。
     */
    private String loginPath = DEF_LOGIN_PATH;
    /**
     * 登出路径。
     */
    private String logoutPath = DEF_LOGOUT_PATH;
    /**
     * 注册路径。
     */
    private String registerPath = DEF_REGISTER_PATH;
    /**
     * 忽略地址列表。
     * <p>
     * 前后端分离通常不需要添加这个配置，这里只是为了以后扩展方便使用。
     */
    private String[] ignorePath = new String[0];
    /**
     * 公开地址列表。
     * <p>
     * 公开的地址表示任何人都可以访问，没有权限相关的限制，通常来说是登录、注册以及报错页面。
     */
    private String[] publicPath = new String[]{DEF_ERROR_PATH, DEF_ERROR_DETAIL_PATH};
}
