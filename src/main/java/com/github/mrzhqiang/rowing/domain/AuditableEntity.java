package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Domains;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 审计实体。
 * <p>
 * 审计信息可以溯源创建人和创建时间，以及最近修改人和最近修改时间。
 * <p>
 * 注意：如果需要记录操作日志，或者统计埋点信息，应单独创建相关数据表，而不是使用审计信息。
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
     * 一般推荐记录 {@link UserDetails#getUsername() 用户名} ，而不是 id 值。
     * <p>
     * 注意：不建议使用 ManyToOne 注解，因为它将导致 Account 在 ToString 时，出现死循环。
     * <p>
     * 要解决这个问题，必须将此字段排除在 ToString 之外。
     */
    @CreatedBy
    @Column(length = Domains.USERNAME_LENGTH)
    private String createdBy;
    /**
     * 创建时间。
     */
    @CreatedDate
    private LocalDateTime created;
    /**
     * 最近修改人。
     * <p>
     * 一般推荐记录 {@link UserDetails#getUsername() 用户名} ，而不是 id 值。
     * <p>
     * 注意：不建议使用 ManyToOne 注解，因为它将导致 Account 在 ToString 时，出现死循环。
     * <p>
     * 要解决这个问题，必须将此字段排除在 ToString 之外。
     */
    @LastModifiedBy
    @Column(length = Domains.USERNAME_LENGTH)
    private String lastModifiedBy;
    /**
     * 最近修改时间。
     */
    @LastModifiedDate
    private LocalDateTime lastModified;

}
