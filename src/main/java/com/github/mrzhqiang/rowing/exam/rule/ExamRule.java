package com.github.mrzhqiang.rowing.exam.rule;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.ExamModeStrategy;
import com.github.mrzhqiang.rowing.exam.mode.ExamMode;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

/**
 * 考试规则。
 * <p>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ExamRule extends AuditableEntity {

    private static final long serialVersionUID = -6502988759907713109L;

    /**
     * 标题。
     */
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String title;
    /**
     * 持续时间(秒)。
     */
    @NotNull
    @Min(15 * 60)
    @Max(180 * 60)
    @Column(nullable = false)
    private Long duration = Duration.ofHours(1).getSeconds();
    /**
     * 总分数。
     */
    @NotNull
    @Min(1)
    @Digits(integer = 3, fraction = 0)
    @Column(nullable = false, precision = 3)
    private Integer totalScore = 100;
    /**
     * 合格线。
     */
    @NotNull
    @Min(0)
    @Max(1)
    @Digits(integer = 1, fraction = 2)
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal passLine = BigDecimal.valueOf(0.60);
    /**
     * 合格分数。
     */
    @NotNull
    @Min(1)
    @Digits(integer = 3, fraction = 0)
    @Column(nullable = false, precision = 3)
    private Integer passScore = 60;
    /**
     * 模式策略。
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_LENGTH)
    private ExamModeStrategy strategy = ExamModeStrategy.RANDOM;

    /**
     * 模式列表。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "rule", orphanRemoval = true)
    private List<ExamMode> modes = Lists.newArrayList();

}
