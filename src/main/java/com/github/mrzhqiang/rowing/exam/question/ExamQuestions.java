package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.helper.Sequences;
import com.github.mrzhqiang.helper.random.RandomStrings;
import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;

/**
 * 试题工具。
 * <p>
 */
@UtilityClass
public class ExamQuestions {

    /**
     * 试题编码前缀。
     */
    public static final String PREFIX_CODE = "EQ";
    /**
     * 试题编码格式化模板。
     */
    public static final String CODE_TEMPLATE = PREFIX_CODE + "-%s-%s-%s";

    /**
     * 通过试题实体生成编码。
     * <p>
     * 预期的编码长度为 32 位字符串，包含字母和数字。
     *
     * @param question 试题实体。
     * @return 编码。可能是完全随机的编码。
     */
    public static String generateCode(ExamQuestion question) {
        // fixation 12 length
        String first = Sequences.ofBasicDateTime();
        // fixation 8 length
        String second = RandomStrings.ofNumber(question.getDifficulty());
        second = Strings.padStart(second, 8, '0');
        // fixation 7 length
        String third = Sequences.ofUid(question.getStem());
        return Strings.lenientFormat(CODE_TEMPLATE, first, second, third);
    }

}
