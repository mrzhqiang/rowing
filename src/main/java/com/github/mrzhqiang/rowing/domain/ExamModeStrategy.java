package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Enums;

/**
 * 考试模式策略。
 * <p>
 * 考虑：未来添加规则引擎，以适应不同规则体系下的考试模式策略。
 */
public enum ExamModeStrategy {

    /**
     * 随机选题。
     */
    RANDOM,
    /**
     * 匹配选题。
     */
    MATCH,
    /**
     * 固定题目。
     */
    FIXED,
    ;

    public static ExamModeStrategy of(String strategy) {
        return Enums.findByNameIgnoreCase(ExamModeStrategy.class, strategy, RANDOM);
    }

}
