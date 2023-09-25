package com.github.mrzhqiang.rowing.exam.paper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * 试卷答题卡。
 * <p>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ExamPaperAnswerCard extends AuditableEntity {

    private static final long serialVersionUID = 2742751378622499666L;

    /**
     * 所属试卷。
     */
    @NotNull
    @ManyToOne(optional = false)
    private ExamPaper paper;
    /**
     * 排序。
     */
    @NotNull
    @Column(nullable = false)
    private Integer ordered = 1;
    /**
     * 标题。
     */
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String title;
    /**
     * 总分数。
     */
    @NotNull
    @Column(nullable = false)
    private BigDecimal totalScore = BigDecimal.ZERO;
    /**
     * 得分和。
     */
    @NotNull
    @Column(nullable = false)
    private BigDecimal sumScore = BigDecimal.ZERO;

    /**
     * 答题列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @OrderBy("ordered asc")
    @OneToMany(mappedBy = "card", orphanRemoval = true)
    private List<ExamPaperAnswer> answers = Lists.newArrayList();

}
