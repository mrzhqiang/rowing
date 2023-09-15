package com.github.mrzhqiang.rowing.session;

import lombok.Data;

import java.io.Serializable;

/**
 * 会话详情。
 * <p>
 * 这个类来自 spring-session-sample-boot-findbyusername/src/main/java/sample/session/SessionDetails.java
 */
@Data
public class SessionDetails implements Serializable {

    private static final long serialVersionUID = 8850489178248613501L;

    /**
     * IP 地址。
     */
    private String ip;
    /**
     * 物理空间位置。
     * <p>
     * 一般是【国家名称 城市名称】格式。
     */
    private String location;
    /**
     * 访问类型。
     * <p>
     * 一般是【操作系统名称 -- 浏览器名称】格式。
     */
    private String accessType;
}
