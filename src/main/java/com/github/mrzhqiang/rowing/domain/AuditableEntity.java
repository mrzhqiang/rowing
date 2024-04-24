package com.github.mrzhqiang.rowing.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 抽象的审计实体。
 * <p>
 * 审计字段用于溯源创建人、创建时间、更新人和更新时间。
 * <p>
 * 注意：如果需要记录操作日志，或者统计埋点信息，应创建对应的数据表，而不是使用审计字段。
 */
@SuppressWarnings("serial")
@Getter
@Setter
@ToString(callSuper = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity extends BaseEntity {

    /**
     * 创建人。
     * <p>
     * 一般记录用户名 ，而不是用户 ID 值。
     * <p>
     * 注意：不建议使用 ManyToOne 注解，因为它将导致 Account 实体在 ToString 时，出现死循环。
     * 要解决这个问题，必须将当前字段排除在 ToString 之外。
     */
    @CreatedBy
    @Column(length = Domains.USERNAME_MAX_LENGTH)
    private String createdBy;
    /**
     * 创建时间。
     */
    @CreatedDate
    private LocalDateTime created;
    /**
     * 更新人。
     * <p>
     * 一般记录用户名 ，而不是用户 ID 值。
     * <p>
     * 注意：不建议使用 ManyToOne 注解，因为它将导致 Account 实体在 ToString 时，出现死循环。
     * 要解决这个问题，必须将当前字段排除在 ToString 之外。
     */
    @LastModifiedBy
    @Column(length = Domains.USERNAME_MAX_LENGTH)
    private String updatedBy;
    /**
     * 更新时间。
     */
    @LastModifiedDate
    private LocalDateTime updated;

}
