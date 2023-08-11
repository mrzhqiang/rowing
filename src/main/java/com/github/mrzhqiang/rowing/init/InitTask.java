package com.github.mrzhqiang.rowing.init;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.domain.TaskType;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 初始化任务。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class InitTask extends AuditableEntity {

    private static final long serialVersionUID = -7502511345270104941L;

    /**
     * 路径。
     */
    @NotBlank
    @Size(max = 500)
    @Column(unique = true, nullable = false, updatable = false, length = 500)
    private String path;
    /**
     * 名称。
     */
    @NotBlank
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String name;
    /**
     * 任务类型。
     */
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = MAX_ENUM_NAME_LENGTH)
    private TaskType type;
    /**
     * 任务状态。
     */
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = MAX_ENUM_NAME_LENGTH)
    private TaskStatus status;
    /**
     * 执行顺序。
     */
    @NonNull
    @Column(nullable = false)
    private Integer ordered;
    /**
     * 是否废弃。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = MAX_ENUM_NAME_LENGTH)
    private Logic discard;

    /**
     * 执行历史。
     */
    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<InitTaskLog> logHistories;

    /**
     * 判断任务是否可执行。
     *
     * @return 返回 true 表示任务可以执行；否则需要忽略执行。
     */
    public boolean isExecutable() {
        return Logic.NO.equals(discard) && TaskStatus.DEFAULT.equals(status);
    }
}
