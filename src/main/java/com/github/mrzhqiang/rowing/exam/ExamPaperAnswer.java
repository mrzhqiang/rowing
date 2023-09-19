package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 试卷作答。
 * <p>
 * 表示在试卷的某道大题中，对某道试题进行作答。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ExamPaperAnswer extends AuditableEntity {

    private static final long serialVersionUID = 2742751378622499666L;

    /**
     * 所属大题。
     */
    @NotNull
    @ManyToOne(optional = false)
    private ExamPaperAnswerGroup group;
    /**
     * 对应试题。
     */
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "question_code", referencedColumnName = "code",
            nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ExamQuestion question;
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
    @JoinTable(name = "answer_options",
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
     * 得分。
     */
    @NotNull
    @Column(nullable = false)
    private BigDecimal score = BigDecimal.ZERO;

}
