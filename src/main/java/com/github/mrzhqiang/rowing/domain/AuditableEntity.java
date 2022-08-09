package com.github.mrzhqiang.rowing.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Nullable;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 可审计抽象实体。
 * <p>
 * 审计信息可以溯源创建人和创建时间，以及最近修改人和最近修改时间。
 * <p>
 * 注意：如果需要记录操作日志，或者统计埋点信息，应单独创建相关数据表，而不是使用审计信息。
 */
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
    @Nullable
    @CreatedBy
    private String createdBy;
    /**
     * 创建时间。
     */
    @CreatedDate
    private Instant created;
    /**
     * 最近修改人。
     * <p>
     * 一般推荐记录 {@link UserDetails#getUsername() 用户名} ，而不是 id 值。
     * <p>
     * 注意：不建议使用 ManyToOne 注解，因为它将导致 Account 在 ToString 时，出现死循环。
     * <p>
     * 要解决这个问题，必须将此字段排除在 ToString 之外。
     */
    @Nullable
    @LastModifiedBy
    private String lastModifiedBy;
    /**
     * 最后修改时间。
     */
    @LastModifiedDate
    private Instant lastModified;

    /**
     * 创建时间。
     * <p>
     * 这是一个 UTC 时间戳，通常应该转为本地时间，所以请调用 {@link #getLocalCreated()} 方法。
     * <p>
     * 本方法为受保护的访问级别，可以避免造成误解。
     *
     * @return UTC 瞬间时刻。
     */
    protected Instant getCreated() {
        return created;
    }

    /**
     * 最后修改时间。
     * <p>
     * 这是一个 UTC 时间戳，通常应该转为本地时间，所以请调用 {@link #getLocalLastModified()} 方法。
     * <p>
     * 本方法为受保护的访问级别，可以避免造成误解。
     *
     * @return UTC 瞬间时刻。
     */
    protected Instant getLastModified() {
        return lastModified;
    }

    /**
     * 本地创建时间。
     * <p>
     * 将 UTC 时间转为本地时区的时间，防止跨时区问题。
     *
     * @return 本地日期时间。
     */
    public LocalDateTime getLocalCreated() {
        return LocalDateTime.ofInstant(this.created, ZoneId.systemDefault());
    }

    /**
     * 本地最后修改时间。
     * <p>
     * 将 UTC 时间转为本地时区的时间，防止跨时区问题。
     *
     * @return 本地日期时间。
     */
    public LocalDateTime getLocalLastModified() {
        return LocalDateTime.ofInstant(this.lastModified, ZoneId.systemDefault());
    }
}
