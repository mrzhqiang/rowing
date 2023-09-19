package com.github.mrzhqiang.rowing.init;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
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
import javax.persistence.OrderBy;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @Size(max = Domains.CLASS_NAME_LENGTH)
    @Column(unique = true, nullable = false, updatable = false, length = Domains.CLASS_NAME_LENGTH)
    private String path;
    /**
     * 名称。
     */
    @NotBlank
    @Size(max = Domains.INIT_TASK_NAME_LENGTH)
    @Column(nullable = false, length = Domains.INIT_TASK_NAME_LENGTH)
    private String name;
    /**
     * 任务类型。
     */
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = Domains.ENUM_NAME_LENGTH)
    private TaskType type;
    /**
     * 任务状态。
     */
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_NAME_LENGTH)
    private TaskStatus status;
    /**
     * 执行顺序。
     */
    @NonNull
    @Column(nullable = false)
    private Integer ordered = 0;
    /**
     * 是否废弃。
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_NAME_LENGTH)
    private Logic discard;

    /**
     * 执行日志。
     */
    @JsonIgnore
    @ToString.Exclude
    @OrderBy("created desc")
    @OneToMany(mappedBy = "task", orphanRemoval = true)
    private List<InitTaskLog> logs;

}
