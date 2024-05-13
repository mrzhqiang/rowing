package com.github.mrzhqiang.rowing.exam.paper;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.exam.question.ExamQuestion;
import com.github.mrzhqiang.rowing.exam.question.ExamQuestionOption;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 试卷答题。
 * <p>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ExamPaperAnswer extends AuditableEntity {

    private static final long serialVersionUID = 2742751378622499666L;

    /**
     * 所属答题卡。
     */
    @NotNull
    @ManyToOne(optional = false)
    private ExamPaperAnswerCard card;
    /**
     * 对应试题。
     */
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "question_code", referencedColumnName = "code",
            nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ExamQuestion question;
    /**
     * 排序。
     */
    @NotNull
    @Column(nullable = false)
    private Integer ordered = 1;
    /**
     * 所选选项。
     * <p>
     * 仅判断题、单选题。
     */
    @ManyToOne
    private ExamQuestionOption selectOption;
    /**
     * 多选选项列表。
     */
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "exam_paper_answer_options",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id"))
    private List<ExamQuestionOption> chooseOptions = Lists.newArrayList();
    /**
     * 回答。
     */
    @Column(length = 2000)
    private String answer;
    /**
     * 回答链接。
     */
    @Column(length = Domains.HTTP_URL_PATH_LENGTH)
    private String answerUrl;
    /**
     * 阅卷批注。
     */
    @Column(length = 500)
    private String comment;
    /**
     * 试题分数。
     */
    @NotNull
    @Column(nullable = false)
    private BigDecimal questionScore = BigDecimal.ZERO;
    /**
     * 试题得分。
     */
    @NotNull
    @Column(nullable = false)
    private BigDecimal score = BigDecimal.ZERO;

}
