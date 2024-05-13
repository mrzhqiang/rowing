package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.ExamQuestionType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 试题。
 * <p>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ExamQuestion extends AuditableEntity {

    private static final long serialVersionUID = 6788788287815113358L;

    /**
     * 所属题库。
     */
    @NotNull
    @ManyToOne(optional = false)
    private ExamQuestionBank bank;

    /**
     * 试题编码。
     * <p>
     * 必填，唯一。
     * <p>
     * 生成算法：根据题型、题库科目以及创建时间等参数生成。
     */
    @NotBlank
    @Column(nullable = false, unique = true)
    private String code;
    /**
     * 题型。
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_LENGTH)
    private ExamQuestionType type;
    /**
     * 难度。
     * <p>
     * 表示难度星级，分为 1--5 星级，1 星最简单，5 星最难。
     */
    private Integer difficulty;
    /**
     * 题干。
     */
    @NotBlank
    @Size(max = 2000)
    @Column(nullable = false, length = 2000)
    private String stem;
    /**
     * 正确选项。
     */
    @OneToOne
    private ExamQuestionOption rightOption;
    /**
     * 是否宽松模式。
     * <p>
     * 默认不开启，需要全选对才计分；
     * 多选题可以开启此模式，表示未选择错误答案时，将已选答案的分值占比乘以试题分数，进行统计求和。
     */
    @Column(columnDefinition = Domains.BOOL_COLUMN_FALSE)
    private Boolean sloppyMode = false;
    /**
     * 答案。
     */
    @Column(length = 2000)
    private String solution;
    /**
     * 答案链接。
     */
    @Column(length = Domains.HTTP_URL_PATH_LENGTH)
    private String solutionUrl;
    /**
     * 解释。
     */
    @Column(length = 2000)
    private String explained;
    /**
     * 解释链接。
     */
    @Column(length = Domains.HTTP_URL_PATH_LENGTH)
    private String explainedUrl;
    /**
     * 备注。
     */
    @Column(length = 500)
    private String remark;

    /**
     * 选项列表。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "question", orphanRemoval = true)
    private List<ExamQuestionOption> options;

}
