package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 考试报告。
 * <p>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ExamReport extends AuditableEntity {

    private static final long serialVersionUID = 183988283572223850L;

    /**
     * 所属考试。
     */
    @NotNull
    @OneToOne(optional = false)
    private Exam exam;

    /**
     * 计划参考人数。
     */
    private Integer planCount;
    /**
     * 实际参考人数。
     */
    private Integer actualCount;
    /**
     * 合格人数。
     */
    private Integer passCount;
    /**
     * 合格率。
     */
    private BigDecimal passRate;
    /**
     * 满分。
     */
    private BigDecimal fullScore;
    /**
     * 最高分。
     */
    private BigDecimal maxScore;
    /**
     * 最低分。
     */
    private BigDecimal minScore;
    /**
     * 平均分。
     */
    private BigDecimal avgScore;
    /**
     * 难度系数。
     * <p>
     * 简单公式：平均分 / 最高分。
     */
    private BigDecimal difficultyRate;

}
