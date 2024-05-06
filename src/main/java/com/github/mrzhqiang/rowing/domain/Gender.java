package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Enums;

/**
 * 性别。
 * <p>
 * 简单分为未知、男、女三种性别。未知是未填写的意思。
 */
public enum Gender {

    /**
     * 未知。
     */
    UNKNOWN,
    /**
     * 男性。
     */
    MALE,
    /**
     * 女性。
     */
    FEMALE,;

    public static Gender of(String code) {
        return Enums.findByNameIgnoreCase(Gender.class, code, UNKNOWN);
    }

}
