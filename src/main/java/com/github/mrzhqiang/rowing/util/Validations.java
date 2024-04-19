package com.github.mrzhqiang.rowing.util;

import lombok.experimental.UtilityClass;
import okhttp3.HttpUrl;
import org.springframework.util.StringUtils;

/**
 * 验证工具。
 */
@UtilityClass
public class Validations {

    /**
     * 有效的 URL 地址。
     *
     * @param url URL 地址字符串。
     * @return 返回 true 表示传入参数为 URL 地址；返回 false 则表示不是。
     */
    public static boolean validUrl(String url) {
        return StringUtils.hasText(url) && HttpUrl.parse(url) != null;
    }

}
