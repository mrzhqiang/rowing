package com.github.mrzhqiang.rowing.util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;

/**
 * 年龄工具的单元测试。
 */
public class AgesTest {

    @Test
    public void ofFullYear() {
        // 2019-10-25 is lucky day
        LocalDate birthday = LocalDate.of(2019, Month.OCTOBER, 25);

        LocalDate now = LocalDate.now();
        Integer expected = now.getYear() - 2019;
        if (now.withYear(2019).isBefore(birthday)) {
            expected--;
        }
        assertEquals(expected, Ages.ofFullYear(birthday));
    }
}
