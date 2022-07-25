package com.github.mrzhqiang.rowing.util;

import com.google.common.base.CharMatcher;

/**
 * 匹配器工具。
 */
public final class Matchers {
    private Matchers() {
        // no instances
    }

    /**
     * 纯数字匹配器。
     */
    public static final CharMatcher PURE_NUMBER = CharMatcher.inRange('0', '9');
}
