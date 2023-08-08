package com.github.mrzhqiang.rowing.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Authority;
import com.github.mrzhqiang.rowing.role.Role;
import com.github.mrzhqiang.rowing.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.compress.utils.Lists;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 账户。
 * <p>
 * 账户包含用户名、密码、授权角色、认证统计信息以及用户资料。
 * <p>
 * 1. 授权角色主要用于区分匿名人员、用户和管理员。
 * <p>
 * 匿名人员访问公共资源，用户访问系统功能，管理员访问后台管理，通常后者包含前者的所有权限。
 * <p>
 * 2. 认证统计信息，可以用来保证账户安全，避免账户密码被暴力破解。
 * <p>
 * 尽管 Spring Security 在密码验证时，已经通过限制验证频率（约 1s）来避免穷举，但加上失败次数超限锁定账户的逻辑，会更人性化一点。
 * <p>
 * 另外，也可以完全实现 {@link UserDetails} 接口，比如：账户是否过期、密码是否过期等，以此保证账户安全。
 * <p>
 * 3. 用户资料，包含昵称、头像等相关信息。
 * <p>
 * 注意：账户在登录后，实例会存储在会话中，因此需要尽可能简单，其他逻辑应交给 {@link User} 进行处理。
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
    @Column(updatable = false, unique = true, nullable = false, length = 24)
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
    @JsonIgnore
    @ToString.Exclude
    @Column(nullable = false)
    private String password;
    /**
     * 权限。
     * <p>
     * 默认的权限是用户角色。
     * <p>
     * 注意：这个角色是后端角色，仅用于区别匿名人员、用户以及管理员使用。
     */
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority = Authority.ROLE_USER;

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
    @JsonIgnore
    private int failedCount = 0;
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
    private boolean disabled = false;

    /**
     * 用户资料。
     */
    @JsonIgnore
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner", orphanRemoval = true)
    private User user;

    /**
     * 获取授权列表。
     * <p>
     * 授权在 Spring Security 中指的是角色+权限。
     * <p>
     * 角色可以用来控制访问 URL 路径，权限可以用来控制访问服务层方法，它俩也可以搭配一起使用。
     * <p>
     * 更细粒度的权限也可以交由 Spring Security ACL 框架来实现，它包含了对数据权限的控制（读、写、创建、删除、管理以及自定义权限）。
     * <p>
     * 有经验的开发者也可以设计一套 AccessDecisionVoter 投票决策，它是上面所有内容的底层支撑接口。
     * <p>
     * 在前后端分离的模式下，这里的授权是用于角色权限和接口权限的限制访问，接口权限来自用户中存储的相关数据。
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = user.getRoleList().stream()
                .flatMap(it -> it.getResourceList().stream())
                .distinct()
                .collect(Collectors.toList());
        authorities.addAll(Optional.ofNullable(authority)
                .map(it -> AuthorityUtils.createAuthorityList(it.name()))
                .orElse(AuthorityUtils.NO_AUTHORITIES));
        return authorities;
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
