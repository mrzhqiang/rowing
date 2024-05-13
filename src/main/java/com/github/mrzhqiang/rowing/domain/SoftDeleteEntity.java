package com.github.mrzhqiang.rowing.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 逻辑实体。
 * <p>
 * 这通常包含一个逻辑字段，用于表示扩展实体的逻辑状态，因为一旦软删除字段为 YES 则表示当前实体已被删除。
 * <p>
 * 请注意：扩展实体需要加上逻辑查询语句，以避免正常情况下将软删除字段为 YES 的数据查询出来。
 * <p>
 * 一个简单的示例如下：
 * <pre>@Where(clause = "deleted = 'NO'")</pre>
 */
@SuppressWarnings("serial")
@Getter
@Setter
@MappedSuperclass
@Where(clause = "deleted = 'NO'")
@EntityListeners(AuditingEntityListener.class)
public abstract class SoftDeleteEntity extends BaseEntity {

    /**
     * 是否已删除。
     * <p>
     * 这个字段是软删除，需要配合软删除相关的仓库方法进行使用。
     * <p>
     * 比如在自定义仓库的软删除方法上添加如下注解：
     * <pre>@Transactional @Modifying @Query("update #{#entityName} e set e.deleted = 'YES' where e.id = ?1")</pre>
     */
    @Enumerated(EnumType.STRING)
    @Column(length = Domains.ENUM_LENGTH)
    private Logic deleted = Logic.NO;
    /**
     * 删除时间。
     * <p>
     * 由于这个逻辑实体只继承了基础实体，所以额外添加审计字段，用来标识软删除时间戳。
     */
    @LastModifiedDate
    private LocalDateTime deletedTime;
    /**
     * 删除人。
     * <p>
     * 由于这个逻辑实体只继承了基础实体，所以额外添加审计字段，用来标识软删除人。
     */
    @LastModifiedBy
    private String deletedBy;

}
