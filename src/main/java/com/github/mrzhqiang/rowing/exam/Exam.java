package com.github.mrzhqiang.rowing.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.dict.DictItem;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.domain.ExamStatus;
import com.github.mrzhqiang.rowing.exam.paper.ExamPaper;
import com.github.mrzhqiang.rowing.exam.report.ExamReport;
import com.github.mrzhqiang.rowing.exam.rule.ExamRule;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 考试。
 * <p>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class Exam extends AuditableEntity {

    private static final long serialVersionUID = 1710848930242996643L;

    /**
     * 标题。
     */
    @NotBlank
    @Column(nullable = false)
    private String title;
    /**
     * 编码。
     */
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true, updatable = false, length = 100)
    private String code;
    /**
     * 科目。
     * <p>
     * TODO 科目或者枚举的一种搜索实现：利用 in 语句加上对应参数列表即可实现。
     * TODO 即当未选择时，传入全部数据列表（主要枚举或字典不支持空入参查全部数据）。
     * TODO 而当选择一个或多个时，对于 in 语句可以过滤无关数据。
     */
    @NotNull
    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private DictItem subject;
    /**
     * 难度。
     */
    private Integer difficulty;
    /**
     * 规则。
     */
    @NotNull
    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ExamRule rule;
    /**
     * 状态。
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_LENGTH)
    private ExamStatus status = ExamStatus.DEFAULT;
    /**
     * 开始时间。
     */
    @NotNull
    @Column(nullable = false)
    private LocalDateTime startTime;
    /**
     * 结束时间。
     */
    @NotNull
    @Column(nullable = false)
    private LocalDateTime endTime;
    /**
     * 描述。
     */
    @Column(length = 500)
    private String description;

    /**
     * 考生名单。
     */
    @JsonIgnore
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "exam_takers",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"exam_id", "account_id"})})
    private List<Account> takers = Lists.newArrayList();
    /**
     * 阅卷人名单。
     */
    @JsonIgnore
    @ToString.Exclude
    @ManyToMany
    @JoinTable(name = "exam_markers",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"),
            uniqueConstraints = {@UniqueConstraint(columnNames = {"exam_id", "account_id"})})
    private List<Account> markers = Lists.newArrayList();
    /**
     * 试卷列表。
     * <p>
     * 通常根据考生名单生成试卷列表。
     */
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "exam", orphanRemoval = true)
    private List<ExamPaper> papers = Lists.newArrayList();
    /**
     * 统计报告。
     */
    @OneToOne(mappedBy = "exam", orphanRemoval = true)
    private ExamReport report;

}
