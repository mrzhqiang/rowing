package com.github.mrzhqiang.rowing.domain;

/**
 * 多语言代码。
 * <p>
 * 包含系统支持的语言代码。
 */
public enum LanguageCode {

    /**
     * 默认。
     * <p>
     * 参考 Security 框架的默认消息文件是英文，为保持统一风格，自定义消息文件的默认代码也应该是英文。
     */
    DEFAULT(""),
    /**
     * 英语。
     */
    EN("en"),
    /**
     * 简体中文。
     */
    ZH_CN("zh_CN"),
    /**
     * 繁体中文。
     */
    ZH_TW("zh_TW"),
    ;

    final String code;

    LanguageCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
