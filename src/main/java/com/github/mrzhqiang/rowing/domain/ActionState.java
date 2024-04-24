package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Enums;

/**
 * 操作状态。
 */
public enum ActionState {

    /**
     * 未知。
     */
    UNKNOWN,
    /**
     * 执行通过。
     */
    PASSING,
    /**
     * 执行失败。
     */
    FAILED,
    ;

    public static ActionState of(String state) {
        return Enums.findByNameIgnoreCase(ActionState.class, state, UNKNOWN);
    }

}
