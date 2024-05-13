package com.github.mrzhqiang.rowing.exam.paper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.exam.Exam;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 试卷。
 * <p>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"exam_code", "taker_id"}))
public class ExamPaper extends AuditableEntity {

    private static final long serialVersionUID = 7541256513151089733L;

    /**
     * 所属考试。
     */
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "exam_code", referencedColumnName = "code",
            nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Exam exam;
    /**
     * 考生。
     */
    @NotNull
    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "taker_id", nullable = false, updatable = false)
    private Account taker;
    /**
     * 阅卷人。
     */
    @NotNull
    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "marker_id", nullable = false)
    private Account marker;
    /**
     * 答题开始时间。
     */
    private LocalDateTime answerStart;
    /**
     * 是否提交。
     */
    @NotNull
    @Column(nullable = false, columnDefinition = Domains.BOOL_COLUMN_FALSE)
    private Boolean submitted = false;
    /**
     * 提交时间。
     */
    private LocalDateTime submitTime;
    /**
     * 阅卷开始时间。
     */
    private LocalDateTime markStart;
    /**
     * 是否完成。
     * <p>
     * 试卷完成意味着试卷的生命周期结束。
     * <p>
     * 需要满足以下两个步骤：
     * <p>
     * 1. 考生在考试期间进行答题，并提交（或考试时间结束自动交卷）。
     * <p>
     * 2. 阅卷人对试卷的回答进行阅卷批注，并完成试卷。
     * <p>
     * 注意：未进入考试且不存在答题内容的试卷，将在考试结束时记为零分，并完成试卷。
     */
    @NotNull
    @Column(nullable = false, columnDefinition = Domains.BOOL_COLUMN_FALSE)
    private Boolean finished = false;
    /**
     * 完成时间。
     */
    private LocalDateTime finishTime;
    /**
     * 结果。
     * <p>
     * 完成阅卷时，根据答题列表及相关模版，生成的成绩总结。
     */
    @Column(length = 1000)
    private String result;
    /**
     * 总得分。
     * <p>
     * 考试人员在当前试卷中的所有答题得分求和。
     */
    @Column(nullable = false)
    private BigDecimal totalScore = BigDecimal.ZERO;

    /**
     * 答题卡列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @OrderBy("ordered asc")
    @OneToMany(mappedBy = "paper", orphanRemoval = true)
    private List<ExamPaperAnswerCard> cards = Lists.newArrayList();

}
