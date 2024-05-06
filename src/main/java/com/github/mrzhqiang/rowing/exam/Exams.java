package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.helper.Sequences;
import com.github.mrzhqiang.helper.random.RandomStrings;
import com.github.mrzhqiang.rowing.domain.ExamStatus;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;

/**
 * 考试工具。
 * <p>
 */
@UtilityClass
public class Exams {

    /**
     * 考试编码前缀。
     */
    public static final String PREFIX_CODE = "EX";
    /**
     * 考试编码格式化模板。
     */
    public static final String CODE_TEMPLATE = PREFIX_CODE + "-%s-%s-%s";

    /**
     * 通过考试实体生成编码。
     * <p>
     * 预期的编码长度为 28--30 位字符串，包含字母和数字。
     *
     * @param exam 考试实体。
     * @return 编码。可能是完全随机的编码。
     */
    public static String generateCode(Exam exam) {
        // 前缀加连字符 5 位长度
        // 固定 12 位长度
        String first = Sequences.ofBasicDateTime();
        // 固定 5 位长度
        String second = RandomStrings.ofNumber(exam.getDifficulty());
        second = Strings.padStart(second, 5, '0');
        // 区间 6--8 位长度
        String third = Sequences.ofUid(exam.getTitle());
        return Strings.lenientFormat(CODE_TEMPLATE, first, second, third);
    }

    /**
     * 验证考试是否可以更新。
     *
     * @param exam 考试实体。
     */
    public static void validateUpdate(Exam exam) {
        Preconditions.checkNotNull(exam, "exam == null");
        Preconditions.checkState(ExamStatus.DEFAULT.equals(exam.getStatus()),
                "当前考试不可修改，请确认考试状态是否为默认");
    }

    /**
     * 验证考试是否可以准备。
     *
     * @param exam 考试实体。
     */
    public static void validateWaiting(Exam exam) {
        Preconditions.checkNotNull(exam, "exam == null");
        Preconditions.checkState(!CollectionUtils.isEmpty(exam.getTakers()),
                "准备考试之前必须添加考生名单");
        Preconditions.checkState(!CollectionUtils.isEmpty(exam.getMarkers()),
                "准备考试之前必须添加阅卷人名单");
        Preconditions.checkNotNull(exam.getRule(),
                "准备考试之前，必须选择考试规则");
        Preconditions.checkState(!CollectionUtils.isEmpty(exam.getRule().getModes()),
                "准备考试之前必须选择存在有效模式列表的考试规则");
    }

    public static boolean validateTake(Exam exam) {
        return ExamStatus.RUNNING.equals(exam.getStatus())
                && LocalDateTime.now().isBefore(exam.getEndTime());
    }

    public static boolean validateMark(Exam exam) {
        return ExamStatus.MARKING.equals(exam.getStatus());
    }

}
