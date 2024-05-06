package com.github.mrzhqiang.rowing.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.role.Role;
import com.github.mrzhqiang.rowing.user.User;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

/**
 * 账户。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account extends AuditableEntity {

    private static final long serialVersionUID = 5162282743335524644L;

    /**
     * 用户名。
     */
    @NotBlank
    @Size(min = Domains.USERNAME_MIN_LENGTH, max = Domains.USERNAME_MAX_LENGTH)
    @Column(updatable = false, unique = true, nullable = false, length = Domains.USERNAME_MAX_LENGTH)
    private String username;
    /**
     * 密码。
     * <p>
     * 明文密码最小长度 {@link Domains#PASSWORD_MIN_LENGTH 6} 位，默认最大长度 {@link Domains#DEF_PASSWORD_MAX_LENGTH 64} 位（支持在系统设置中修改），可以是任意字符。
     * <p>
     * 密码绝对禁止明文存储，推荐通过 {@link PasswordEncoder} 的 {@link BCryptPasswordEncoder} 实现类进行随机盐加密处理。
     * <p>
     * 由于是加密存储，这里无法指定字段长度，只能交给服务层进行处理。
     */
    @NotBlank
    @JsonIgnore()
    @ToString.Exclude
    @Column(nullable = false)
    private String password;
    /**
     * 账户类型。
     * <p>
     * 必填，默认是 {@link AccountType#USER 用户} 类型。
     * <p>
     * 用来区分游客、用户、管理员。游客访问公共资源，用户访问系统功能，管理员访问后台管理，通常后者包含前者的所有权限。
     */
    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_LENGTH)
    private AccountType type = AccountType.USER;
    /**
     * 账户失效期限。
     * <p>
     * 如果这个字段未设置，则表示账户永久有效。
     * <p>
     * 否则账户在登录时，将检测当前日期是否已超过失效期限，超过表示账户已失效，需要重新激活才能使用。
     */
    private Instant expired;
    /**
     * 登录失败次数统计。
     * <p>
     * 账户未锁定时，统计登录失败的次数，当次数达到阈值时，将锁定账户一段时间。
     * <p>
     * 账户锁定之后，不再统计次数，也不允许登录，直到锁定时间过期。
     */
    @Builder.Default
    private Integer failedCount = 0;
    /**
     * 锁定时间戳。
     * <p>
     * 这个时间戳属于未来时间，即当前时间 > 锁定时间戳，则表示账户未锁定，否则表示账户已锁定。
     */
    private Instant locked;
    /**
     * 是否强制锁定。
     * <p>
     * 一旦设置强制锁定，则表示账户直接锁定，不再计算锁定时间戳。
     */
    @Builder.Default
    private Boolean forcedLocked = false;
    /**
     * 密码失效期限。
     * <p>
     * 如果这个字段未设置，则表示密码永久有效。
     * <p>
     * 否则账户在登录时，将检测当前日期是否已超过失效期限，超过表示密码已失效，需要修改密码才能使用。
     */
    private Instant passwordExpired;
    /**
     * 是否禁用。
     * <p>
     * 禁用比锁定更严重，因为这意味着，如果不手动启用，账户将始终无法使用。
     */
    @Builder.Default
    private Boolean disabled = false;
    /**
     * 用户。
     * <p>
     * 一对一关联的用户，通常是昵称、性别这些数据。
     */
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "owner", orphanRemoval = true)
    private User user;

    /**
     * 账户拥有的角色集合。
     */
    @Builder.Default
    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(mappedBy = "accounts")
    private Set<Role> roles = Sets.newHashSet();

}
