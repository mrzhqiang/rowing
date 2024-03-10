package com.github.mrzhqiang.rowing.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import com.github.mrzhqiang.rowing.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

/**
 * 账户。
 * <p>
 * 账户包含用户名、密码、账户类型、用户信息、权限信息等字段。
 * <p>
 * 1. {@link AccountType 账户类型} 可以区分游客、用户、管理员。
 * <p>
 * 游客访问公共资源，用户访问系统功能，管理员访问后台管理，通常后者包含前者的所有权限。
 * <p>
 * 2. {@link UserDetails 安全相关的用户信息} 可以用来保护账户安全。
 * <p>
 * 2.1 认证时检测：用户名和密码是否正确、账户是否未过期、账户是否未锁定、密码是否未过期、账户是否启用。
 * <p>
 * 2.2 授权时检测：对于受限制的方法，判断规则（角色、授权、特定以及权限等）是否允许执行。
 * <p>
 * 3. {@link User 用户信息} 包含更丰富的信息，比如：昵称、头像、性别、生日、简介等等。
 * <p>
 * 用户信息可以自由扩展字段，不影响账户，甚至可以新建其他的信息来替代。
 * <p>
 * 4. 权限信息包含当前账户拥有的权限。
 * <p>
 * 权限信息由账户类型对应的权限以及角色对应的权限列表组成，最多不超过 2000 字符，通常以 , 符号分割不同的权限。
 * <p>
 * 因此，当账户权限发生变动时，需要调用 onAfterSave 回调更新账户的权限信息。
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
     * 1. 注册创建。校验规则：最小长度 4 位，最大长度 24 位，必须为字母或数字，且以字母开头，字母或数字结尾，仅允许 _ 特殊符号。
     * <p>
     * 2. 根据学号生成。生成规则：
     * <p>
     * 2.1 以 sid_ 为前缀，表示学号类型；
     * <p>
     * 2.2 如果学号长度不超过 20 个字符，直接追加到前缀后面；超过则判断是否为纯数字，是就截取后 20 个字符追加到前缀后面。
     * <p>
     * 2.3 如果学号不是纯数字且超过 20 个字符，那么将学号 hash code 后，格式化为 12 位数字的字符串作为中缀；
     * <p>
     * 2.3.1 生成 4 位纯数字的随机字符串作为后缀，通过 _ 符号分割。
     * <p>
     * 2.3.2 如果生成的用户名重复，则在最后追加 00 字符，再使用 _ 符号分割；如果依旧重复，则追加到 99 为止。
     * <p>
     * 3. 其他规则：类似学生学号，结合前缀、中缀、后缀以及递增补位，共同组成用户名。
     * <p>
     * 注意：任何第三方用户的唯一标识符，必须冗余到某个地方，比如第三方用户实体，方便下次认证直接通过，无需再注册账户。
     */
    @NotBlank
    @Size(min = Domains.USERNAME_MIN_LENGTH, max = Domains.USERNAME_MAX_LENGTH)
    @Column(updatable = false, unique = true, nullable = false, length = Domains.USERNAME_MAX_LENGTH)
    private String username;
    /**
     * 密码。
     * <p>
     * 创建时指定，必须加密存储。
     * <p>
     * 密码明文最小长度 6 位，最大长度 64 位，可以是任意字符。
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
    @Column(nullable = false, length = Domains.ENUM_LENGTH)
    private AccountType type = AccountType.USER;
    /**
     * 权限信息。
     * <p>
     * 表示账户对应的权限，可以是角色和权限的组合，使用 , 符号进行拼接。
     */
    @Builder.Default
    @Column(nullable = false, length = Domains.AUTHORITIES_LENGTH)
    private String authority = AccountType.USER.getAuthority();
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
     * 用户。
     * <p>
     * 一对一关联的用户，通常是昵称、性别这些数据。
     */
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "owner", orphanRemoval = true)
    private User user;

    /**
     * 获取授权列表。
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
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
