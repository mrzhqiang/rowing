package com.github.mrzhqiang.rowing.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@ConfigurationProperties("rowing.security")
public class RowingSecurityProperties {

    /**
     * 默认的登录路径。
     */
    private static final String DEF_LOGIN_PATH = "/login";
    /**
     * 默认的注册路径。
     */
    private static final String DEF_REGISTER_PATH = "/register";

    /**
     * 登录路径。
     */
    private String loginPath = DEF_LOGIN_PATH;
    /**
     * 注册路径。
     */
    private String registerPath = DEF_REGISTER_PATH;
}
