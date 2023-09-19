package com.github.mrzhqiang.rowing.exam;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * 试题分组。
 * <p>
 * 表示此类试题在试卷中的分组，俗称为大题。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ExamPaperAnswerGroup extends AuditableEntity {

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
    private Integer ordered;
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
     * 作答列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "group", orphanRemoval = true)
    private List<ExamPaperAnswer> answers = Lists.newArrayList();

}
