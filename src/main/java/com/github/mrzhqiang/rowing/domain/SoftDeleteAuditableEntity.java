package com.github.mrzhqiang.rowing.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

/**
 * 抽象的软删除审计实体。
 * <p>
 * 这个实体将软删除实体与审计实体结合起来使用。
 */
@SuppressWarnings("serial")
@Getter
@Setter
@ToString(callSuper = true)
@MappedSuperclass
@Where(clause = "deleted = 'NO'")
public abstract class SoftDeleteAuditableEntity extends AuditableEntity {

    /**
     * 是否已删除。
     * <p>
     * 这个字段是软删除，需要配合软删除相关的仓库方法进行使用。
     * <p>
     * 比如在自定义仓库的软删除方法上添加如下注解：
     * <pre>@Transactional @Modifying @Query("update #{#entityName} e set e.deleted = 'YES' where e.id = ?1" and e.deleted = 'NO')</pre>
     */
    @Enumerated(EnumType.STRING)
    @Column(length = Domains.ENUM_LENGTH)
    private Logic deleted = Logic.NO;

}
