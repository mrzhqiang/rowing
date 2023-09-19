package com.github.mrzhqiang.rowing.domain;

/**
 * 考试模式策略。
 * <p>
 * 实际上应该叫考试选题策略。
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

}
