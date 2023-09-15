package com.github.mrzhqiang.rowing.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.role.Role;
import com.github.mrzhqiang.rowing.third.ThirdUser;
import com.github.mrzhqiang.rowing.user.User;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 账户。
 * <p>
 * 账户包含用户名、密码、账户类型、用户信息、角色列表等字段。
 * <p>
 * 1. {@link AccountType 账户类型} 可以区分游客、用户、管理员。
 * <p>
 * 游客访问公共资源，用户访问系统功能，管理员访问后台管理，通常后者包含前者的所有权限。
 * <p>
 * 2. {@link UserDetails 安全相关的用户信息} 可以用来保护账户安全。
 * <p>
 * 2.1 认证时检测：用户名、密码是否正确，账户是否未过期、账户是否未锁定、密码是否未过期、账户是否启用。
 * <p>
 * 2.2 授权时检测：对于受限制的方法，是否包含指定角色或权限。
 * <p>
 * 3. {@link User 非安全相关的用户信息} 包含更丰富的资料，比如：昵称、头像、性别、生日、简介等等。
 * <p>
 * 非安全相关的用户信息支持扩展字段，完全不影响账户，甚至可以被新的用户信息替换。
 * <p>
 * 4. {@link com.github.mrzhqiang.rowing.role.Role 角色列表} 包含当前账户拥有的菜单及菜单资源。
 * <p>
 * 角色列表可以视为权限列表，只要账户属于某个角色，即获得角色对应的所有菜单及菜单资源（权限）。
 * <p>
 * 这种数据结构非常方便对账户进行授权和取消授权，避免重复且繁琐的操作。
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Account extends AuditableEntity implements UserDetails {

    private static final long serialVersionUID = 5162282743335524644L;

    /**
     * 用户名。
     * <p>
     * 1. 注册创建。校验规则：最小长度 4 位，最大长度 24 位，必须为全小写字母，且以字母开头，字母或数字结尾，不包含特殊符号。
     * <p>
     * 2. 根据学生学号生成。生成规则：
     * <p>
     * 2.1 以 sid_ 为前缀，表示学生编号类型；
     * <p>
     * 2.2 将学生学号 hash code 后取其中四位数字作为中缀；
     * <p>
     * 2.3 生成最小长度 12 位，最大长度 16 位，包含 [大小写字母 + 数字 + 特殊字符] 的随机字符串，作为后缀。
     * <p>
     * 3. 其他规则：类似学生学号，结合前缀+中缀+随机字符串后缀，组成用户名。
     */
    @NotBlank
    @Size(max = Domains.USERNAME_LENGTH)
    @Column(updatable = false, unique = true, nullable = false, length = Domains.USERNAME_LENGTH)
    private String username;
    /**
     * 密码。
     * <p>
     * 创建时指定，必须加密存储，最小长度 6 位，最大长度 64 位，可以是任意字符。
     * <p>
     * 密码绝对不允许明文存储，通过 Spring Security Crypto 的 PasswordEncoder 类进行随机盐编码处理。
     * <p>
     * 由于是加密存储，这里无法指定字段长度，只能交给服务层进行处理。
     */
    @NotBlank
    @JsonIgnore()
    @ToString.Exclude
    @Column(nullable = false)
    private String password;
    /**
     * 类型。
     * <p>
     * 必填，默认是用户类型。
     */
    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = Domains.ENUM_NAME_LENGTH)
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
     * 失败次数统计。
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
     * 用户信息。
     * <p>
     * 一对一关联的用户信息，属于账户的扩展数据。
     */
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "owner", orphanRemoval = true)
    private User user;
    /**
     * 第三方用户列表。
     * <p>
     * 一对多关联的第三方用户列表，通常是第三方平台注册本系统账户时关联的第三方用户信息。
     */
    @JsonIgnore
    @Builder.Default
    @ToString.Exclude
    @OneToMany(mappedBy = "account", orphanRemoval = true)
    private List<ThirdUser> thirdUsers = Lists.newArrayList();
    /**
     * 角色列表。
     * <p>
     * 实际上表示账户拥有的菜单及菜单资源列表，换句话说，就是账户的权限列表。
     */
    @JsonIgnore
    @Builder.Default
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "account_roles",
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "role_id"}))
    private List<Role> roles = Lists.newArrayList();

    /**
     * 获取授权列表。
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.concat(Stream.of(type.toAuthority()),
                        roles.stream().flatMap(it -> it.getGrantedAuthorities().stream()))
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 账户是否未过期。
     * <p>
     * 对安全性要求较高的系统，可能会选择实现账户过期的策略。
     * <p>
     * 比如给用户的账户设置半年期限，如果到期则不能访问系统，除非申请账户延期。
     * <p>
     * 通常在账户到期时，需要注销所有在线会话。
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return Optional.ofNullable(expired)
                .map(Instant.now()::isBefore)
                .orElse(true);
    }

    /**
     * 账户是否未锁定。
     * <p>
     * 锁定分为自动锁定和手动锁定两种。
     * <p>
     * 自动锁定：一般是登录时短时间内多次密码错误，将自动锁定一段时间，不再进行密码验证，等下次登录成功自动解锁。
     * <p>
     * 手动锁定：由管理员手动锁定账户，必须手动解锁才能恢复正常。这是一个额外的锁定状态字段，被锁定的账户必须立即注销所有在线会话。
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return Optional.ofNullable(locked)
                .map(Instant.now()::isAfter)
                .orElse(true);
    }

    /**
     * 凭证（密码）是否未过期。
     * <p>
     * 在安全性较高的系统中，一般要求定期修改密码，比如每三个月修改一次密码。
     * <p>
     * 因此，如果检测到密码过期，则要求用户必须更改密码并重新登录。
     * <p>
     * 更改密码一般会导致关联的所有应用失效，特别是在以当前账户系统作为认证源的情况下，此时需要权衡利弊，以决定是否启用该机制。
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return Optional.ofNullable(passwordExpired)
                .map(Instant.now()::isBefore)
                .orElse(true);
    }

    /**
     * 是否启用。
     * <p>
     * 在安全性较高的系统中，账户由申请流程自动创建，或由管理员手动创建，此时账户属于未启用状态。
     * <p>
     * 为启用此账户，必须在下发的邮件通知中，点击激活链接进行启用。
     * <p>
     * 激活链接一般保持 24 小时的有效期，一旦过期则需要管理员手动管理。
     * <p>
     * 如果这个状态由管理员手动管理，在禁用和启用时，都将发送一条邮件通知。
     */
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return !disabled;
    }

}
