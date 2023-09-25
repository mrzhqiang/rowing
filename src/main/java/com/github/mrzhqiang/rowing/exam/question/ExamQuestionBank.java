package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.dict.DictItem;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 题库。
 * <p>
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ExamQuestionBank extends AuditableEntity {

    private static final long serialVersionUID = 5581651091162373289L;

    /**
     * 标题。
     */
    @NotBlank
    @Column(nullable = false)
    private String title;
    /**
     * 科目。
     */
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, updatable = false)
    private DictItem subject;
    /**
     * 描述。
     */
    private String description;

    /**
     * 试题列表。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "bank", orphanRemoval = true)
    private List<ExamQuestion> questions = Lists.newArrayList();

}
