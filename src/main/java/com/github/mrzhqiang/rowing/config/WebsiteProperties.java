package com.github.mrzhqiang.rowing.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * TODO 改为 SysSetting 方便后台管理
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("website")
public class WebsiteProperties {

    /**
     * 网站作者。
     */
    private String author;
    /**
     * 网站图标。
     */
    private String favicon;
    /**
     * 网站标题。
     */
    private String title;
}
