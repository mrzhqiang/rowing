package com.github.mrzhqiang.rowing.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统安全属性。
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("rowing.security")
public class RowingSecurityProperties {

    /**
     * 管理路径。
     */
    public static final String ADMIN_PATH = "/admin";
    /**
     * 子路径。
     */
    public static final String SUB_PATH = "/**";
    /**
     * 资源路径。
     */
    public static final String EXPLORER_PATH = "/explorer";
    /**
     * 基础认证角色。
     */
    public static final String ROLE_BASIC = "BASIC";

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
    private static final String DEF_ERROR_DETAIL_PATH = DEF_ERROR_PATH + SUB_PATH;
    /**
     * 执行器路径。
     */
    private static final String ACTUATOR_PATH = "/actuator";
    /**
     * 执行器子路径。
     */
    private static final String ACTUATOR_SUB_PATH = ACTUATOR_PATH + SUB_PATH;

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
    private String[] publicPath = new String[]{DEF_LOGIN_PATH, DEF_LOGOUT_PATH, DEF_REGISTER_PATH, DEF_ERROR_PATH, DEF_ERROR_DETAIL_PATH};
    /**
     * 需要基础认证的地址列表。
     */
    private String[] basicPath = new String[]{ACTUATOR_PATH, ACTUATOR_SUB_PATH};

}
