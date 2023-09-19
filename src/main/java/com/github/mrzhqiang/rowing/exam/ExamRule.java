package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.ExamModeStrategy;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.convert.DurationUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
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
     * 持续时间。
     */
    @NotNull
    @DurationUnit(ChronoUnit.SECONDS)
    @Column(nullable = false)
    private Duration duration = Duration.ofSeconds(3600);
    /**
     * 总分数。
     */
    @NotNull
    @Column(nullable = false, precision = 3)
    private Integer totalScore = 100;
    /**
     * 合格线。
     */
    @NotNull
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal passLine = BigDecimal.valueOf(0.60);
    /**
     * 合格分数。
     */
    @NotNull
    @Column(nullable = false, precision = 3)
    private Integer passScore = 60;
    /**
     * 策略。
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_NAME_LENGTH)
    private ExamModeStrategy strategy = ExamModeStrategy.RANDOM;

    /**
     * 模式列表。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "rule", orphanRemoval = true)
    private List<ExamMode> modes = Lists.newArrayList();

}
