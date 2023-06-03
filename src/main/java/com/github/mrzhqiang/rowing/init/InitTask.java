package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.entity.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import com.github.mrzhqiang.rowing.domain.TaskType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * 初始化任务。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class InitTask extends AuditableEntity {

    /**
     * 路径。
     */
    @Column(unique = true, nullable = false, updatable = false)
    private String path;
    /**
     * 名称。
     */
    @Column(nullable = false)
    private String name;
    /**
     * 任务类型。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TaskType type;
    /**
     * 任务状态。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
    /**
     * 执行顺序。
     */
    @Column(nullable = false)
    private Integer ordered;
    /**
     * 是否废弃。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Logic discard;

    /**
     * 执行历史。
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "task")
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
