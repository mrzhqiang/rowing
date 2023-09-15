package com.github.mrzhqiang.rowing.domain;

import lombok.Getter;

/**
 * 第三方用户类型。
 * <p>
 * 主要用来区分第三方平台的用户，通常它们在本系统中，拥有不一样的用户名前缀。
 */
@Getter
public enum ThirdUserType {

    /**
     * 学生。
     */
    STUDENT("sid_"),
    /**
     * 教师。
     */
    TEACHER("tid_"),
    /**
     * 微信。
     */
    WECHAT("wx_"),
    /**
     * QQ。
     */
    QQ("qq_"),
    /**
     * 微博。
     */
    WEIBO("wb_"),
    /**
     * 支付宝。
     */
    ALIPAY("ap_"),
    /**
     * 谷歌。
     */
    GOOGLE("gg_"),
    /**
     * 推特。
     */
    TWITTER("tt_"),
    ;

    private final String prefix;

    ThirdUserType(String prefix) {
        this.prefix = prefix;
    }
}
