package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.helper.text.CommonSymbols;
import com.google.common.base.Splitter;
import lombok.Getter;
import org.springframework.util.StringUtils;

import jakarta.annotation.Nullable;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * 第三方用户类型。
 * <p>
 * 主要用来区分第三方平台的用户，通常它们在本系统中拥有不一样的 username 前缀，且长度不超过 3 个字符。
 */
@Getter
public enum ThirdUserType {

    /**
     * 学生。
     */
    STUDENT("sid"),
    /**
     * 教师。
     */
    TEACHER("tid"),
    /**
     * 微信。
     */
    WECHAT("wx"),
    /**
     * QQ。
     */
    QQ("qq"),
    /**
     * 微博。
     */
    WEIBO("wb"),
    /**
     * 支付宝。
     */
    ALIPAY("ap"),
    /**
     * 谷歌。
     */
    GOOGLE("gg"),
    /**
     * 推特。
     */
    TWITTER("tt"),
    ;

    /**
     * 第三方平台生成用户名时的分离器。
     * <p>
     * 通常为下划线 _ 符号，也要看 Account 的 username 字段的正则表达式校验规则。
     */
    public static final Splitter SPLITTER = Splitter.on(CommonSymbols.UNDERLINE).trimResults().omitEmptyStrings();

    private final String prefix;

    ThirdUserType(String prefix) {
        this.prefix = prefix;
    }

    @Nullable
    public static ThirdUserType of(String username) {
        if (StringUtils.hasText(username)) {
            Iterator<String> iterator = SPLITTER.split(username).iterator();
            if (iterator.hasNext()) {
                String first = iterator.next();
                return Stream.of(values())
                        .filter(it -> it.prefix.equals(first))
                        .findFirst()
                        .orElse(null);
            }
        }
        return null;
    }

}
