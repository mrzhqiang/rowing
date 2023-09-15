package com.github.mrzhqiang.rowing.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * 逻辑审计实体。
 * <p>
 * 这个实体将逻辑实体与审计实体结合起来使用。
 */
@Getter
@Setter
@ToString(callSuper = true)
@MappedSuperclass
@Where(clause = "deleted = 'NO'")
public abstract class LogicAuditableEntity extends AuditableEntity {

    /**
     * 是否已删除。
     * <p>
     * 这个字段是逻辑删除，需要配合逻辑删除相关的仓库方法进行使用。
     * <p>
     * 比如在自定义仓库的逻辑删除方法上添加如下注解：
     * <pre>@Transactional @Modifying @Query("update #{#entityName} e set e.deleted = 'YES' where e.id = ?1")</pre>
     */
    @Enumerated(EnumType.STRING)
    private Logic deleted = Logic.NO;
}
