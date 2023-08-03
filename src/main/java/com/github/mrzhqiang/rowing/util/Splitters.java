package com.github.mrzhqiang.rowing.util;

import com.google.common.base.Splitter;

/**
 * 分离器工具。
 */
public final class Splitters {
    private Splitters() {
        // no instances
    }

    /**
     * 逗号（半角）分离器。
     */
    public static final Splitter COMMA = Splitter.on(',').trimResults();
    /**
     * 句号（半角）分离器。
     */
    public static final Splitter DOT = Splitter.on('.').trimResults();
}
