package com.github.mrzhqiang.rowing.domain;

import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * 考试题型。
 * <p>
 */
public enum ExamQuestionType {

    /**
     * 逻辑判断题。
     */
    LOGIC,
    /**
     * 单选题。
     */
    SINGLE,
    /**
     * 多选题。
     */
    MULTIPLE,
    /**
     * 填空题。
     */
    FILL_IN,
    /**
     * 计算题。
     */
    CALCULATION,
    /**
     * 简答题。
     */
    ANSWER,
    /**
     * 论述题。
     */
    ESSAY,
    /**
     * 分析题。
     */
    ANALYTICAL,
    /**
     * 写作题。
     */
    WRITING,
    ;

    private static final List<ExamQuestionType> OBJECTIVE_LIST = ImmutableList.of(LOGIC, SINGLE, MULTIPLE);

    /**
     * 是否客观题。
     *
     * @param type 题型。
     * @return 返回 true 表示题型为客观题；否则为主观题。
     */
    public static boolean isObjective(ExamQuestionType type) {
        return OBJECTIVE_LIST.contains(type);
    }

    /**
     * 是否单个选项。
     *
     * @param type 题型。
     * @return 返回 true 表示题型为单个选项；否则为多个选项或没有任何选项。
     */
    public static boolean isSingleOption(ExamQuestionType type) {
        return LOGIC.equals(type) || SINGLE.equals(type);
    }

}
