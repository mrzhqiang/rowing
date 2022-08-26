package com.github.mrzhqiang.rowing.util;

import com.google.common.base.Preconditions;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * 年龄工具。
 * <p>
 * 主要用于计算公历年龄的各种方法。
 */
public final class Ages {
    private Ages() {
        // no instances
    }

    /**
     * 根据生日日期获得周岁。
     *
     * @param birthday 生日日期。
     * @return 周岁。今年未过生日，年份相差 -1 为周岁；今年已过生日，年份相差直接为周岁。
     */
    public static Integer ofFullYear(LocalDate birthday) {
        return Optional.ofNullable(birthday)
                .map(Ages::convertFull)
                .orElse(0);
    }

    /**
     * 转换周岁。
     * <p>
     * 根据百度百科的介绍，周岁以出生年为 0 岁，第二年过完生日为 1 岁，以此类推。
     * <p>
     * 因此将现在的年份与生日的年份相减，得到的岁数还必须以今年是否过生日为基准，过了不做改动，没过则 -1 岁。
     *
     * @param birthday 生日日期。
     * @return 周岁。
     */
    private static Integer convertFull(LocalDate birthday) {
        Preconditions.checkNotNull(birthday, "birthday == null");

        LocalDate now = LocalDate.now();
        // 计算生日到现在的年份差值——岁数
        int age = (int) ChronoUnit.YEARS.between(birthday, now);
        // 得到今年的生日日期
        LocalDate thisYearBirthday = birthday.withYear(now.getYear());
        // 如果今年还没过生日，说明未满周岁，需要将岁数 -1 处理
        if (now.isAfter(thisYearBirthday)) {
            age--;
        }
        // 满周岁则直接返回
        return age;
    }
}
