package com.github.mrzhqiang.rowing.exam.mode;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.ExamQuestionType;
import com.github.mrzhqiang.rowing.exam.question.ExamQuestion;
import com.github.mrzhqiang.rowing.exam.rule.ExamRule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 考试模式。
 * <p>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ExamMode extends AuditableEntity {

    private static final long serialVersionUID = 1250970576228098394L;

    /**
     * 所属规则。
     */
    @NotNull
    @ManyToOne(optional = false)
    private ExamRule rule;

    /**
     * 排序。
     */
    @NotNull
    @Column(nullable = false)
    private Integer ordered = 1;
    /**
     * 题型。
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_NAME_LENGTH)
    private ExamQuestionType type;
    /**
     * 分值。
     */
    @NotNull
    @Column(precision = 5, scale = 2, nullable = false)
    private BigDecimal score;
    /**
     * 题量。
     */
    @NotNull
    @Column(nullable = false)
    private Integer amount;
    /**
     * 试题一。
     * <p>
     * 当策略为固定时有效。
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_code1", referencedColumnName = "code",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ExamQuestion question1;
    /**
     * 试题二。
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_code2", referencedColumnName = "code",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ExamQuestion question2;
    /**
     * 试题三。
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_code3", referencedColumnName = "code",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ExamQuestion question3;
    /**
     * 试题四。
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_code4", referencedColumnName = "code",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ExamQuestion question4;
    /**
     * 试题五。
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_code5", referencedColumnName = "code",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ExamQuestion question5;

}
