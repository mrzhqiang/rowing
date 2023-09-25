package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * 试题选项。
 * <p>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ExamQuestionOption extends AuditableEntity {

    private static final long serialVersionUID = -8862933124100545540L;

    /**
     * 所属试题。
     */
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "question_code", referencedColumnName = "code",
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ExamQuestion question;

    /**
     * 标签。
     * <p>
     * 比如 A B C 亦或者 1 2 3 之类的标签。
     */
    @NotBlank
    @Size(max = 10)
    @Column(nullable = false, length = 10)
    private String label;
    /**
     * 内容。
     * <p>
     * 标签之后跟随的内容，默认情况下，标签与内容之间，使用顿号 '、' 分割。
     */
    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String content;
    /**
     * 是否正确。
     * <p>
     * 默认为正确答案，主要是为了确保至少存在一个正确答案。
     */
    @Column(columnDefinition = Domains.BOOL_COLUMN_TRUE)
    private Boolean righted = true;
    /**
     * 分值占比。
     * <p>
     * 当开启宽松模式且为多选题时，将分值占比乘以试题分数，即表示当前选项的分数。
     */
    @Column(precision = 3, scale = 2)
    private BigDecimal scoreRatio = BigDecimal.valueOf(0.00);

}
