package com.github.mrzhqiang.rowing.util;

import com.google.common.base.Joiner;

/**
 * 聚合器工具。
 */
public final class Joiners {
    private Joiners() {
        // no instances
    }

    /**
     * 消息的聚合器。
     */
    public static final Joiner MESSAGE = Joiner.on(" - ").skipNulls();
    /**
     * 缓存的聚合器。
     */
    public static final Joiner CACHE = Joiner.on(':').skipNulls();
}
