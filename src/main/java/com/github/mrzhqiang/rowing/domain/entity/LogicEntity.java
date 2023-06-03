package com.github.mrzhqiang.rowing.domain.entity;

import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.domain.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 逻辑实体。
 * <p>
 * 这通常包含一个逻辑字段，用于表示扩展实体的逻辑状态，因为一旦逻辑删除字段为 YES 则表示当前实体已被删除。
 * <p>
 * 请注意：扩展实体需要加上逻辑查询语句，以避免正常情况下将逻辑删除字段为 YES 的数据查询出来。
 * <p>
 * 一个简单的示例如下：
 * <pre>@Where(clause = "logic = 'NO'")</pre>
 */
@Getter
@Setter
@MappedSuperclass
@Where(clause = "deleted = 'NO'")
@EntityListeners(AuditingEntityListener.class)
public abstract class LogicEntity extends BaseEntity {

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

    /**
     * 删除时间。
     * <p>
     * 由于这个逻辑实体只继承了基础实体，所以额外添加审计字段，用来标识逻辑删除时的时间戳。
     */
    @LastModifiedDate
    private LocalDateTime deletedTime;
}
