package com.github.mrzhqiang.rowing.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Authority;
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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

/**
 * 账号。
 * <p>
 * 可以扩展为通过【学生学号】、【教师编号】登录，或者其他凭证如【手机号】、【微信号】等进行登录。
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
     * 学生账号前缀。
     */
    public static final String STUDENT_ID_PREFIX = "sid_";
    /**
     * 教师账号前缀。
     */
    public static final String TEACHER_ID_PREFIX = "tid_";

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
     * 2.3 生成最小长度 12 位，最大长度 16 位，包含大小写字母 + 数字 + 特殊字符的随机字符串，作为后缀。
     * <p>
     * 3. 根据教师编号生成。生成规则：
     * <p>
     * 3.1 以 tid_ 为前缀，表示教师编号类型；
     * <p>
     * 3.2 将教师编号 hash code 后取其中四位数字作为中缀；
     * <p>
     * 3.3 生成最小长度 12 位，最大长度 16 位，包含大小写字母 + 数字 + 特殊字符的随机字符成，作为后缀。
     */
    @Column(updatable = false, unique = true, nullable = false, length = 24)
    private String username;
    /**
     * 密码。
     * <p>
     * 创建时指定，必须加密存储，最小长度 6 位，最大长度 64 位，可以是任意字符。
     * <p>
     * 密码绝对不允许明文存储，通过 Spring Security Crypto 的 PasswordEncoder 类进行随机盐编码处理。
     */
    @JsonIgnore
    @ToString.Exclude
    @Column(nullable = false, length = 64)
    private String password;
    /**
     * 账号所属角色。
     * <p>
     * 注意：这个角色是后端角色，仅用于区别匿名用户、普通用户以及管理员使用，不作为菜单路由和资源权限的标识符。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;
    /**
     * 失败次数统计。
     * <p>
     * 账号未锁定时，统计登录失败的次数，当次数达到阈值时，将锁定账号一段时间，期间不再统计次数，也不允许登录，直到锁定时间过期。
     */
    private int failedCount = 0;
    /**
     * 锁定时间戳。
     * <p>
     * 这个时间戳属于未来时间，即当前时间 > 锁定时间戳，则表示账号未锁定，否则表示账号已锁定。
     */
    private Instant locked;
    /**
     * 是否禁用。
     * <p>
     * 禁用比锁定更严重，因为这意味着，如果不手动启用，账户将始终无法使用。
     */
    private boolean disabled;

    /**
     * 账号对应的用户。
     * <p>
     * 用户通常比账号具有更丰富的特征，比如昵称、头像、性别、生日等等。
     * <p>
     * 另外，还可以将前端的菜单路由和资源权限，存储在用户维度，避免影响后端的角色权限。
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private User user;

    /**
     * 获取授予权限列表。
     * <p>
     * 授予权限在 Spring Security 中指的是角色。
     * <p>
     * 角色可以用来控制访问 URL 路径，即菜单权限。
     * <p>
     * 角色也可以用来控制访问服务层方法，即按钮权限。
     * <p>
     * 更细粒度的权限也可以交由 Spring Security ACL 框架来实现，它包含了对数据权限的控制（读、写、创建、删除、管理以及自定义权限）。
     * <p>
     * 当然，有经验的开发者也可以设计一套 AccessDecisionVoter 投票决策，它是上面所有内容的底层支撑接口。
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(authority)
                .map(it -> AuthorityUtils.createAuthorityList(it.name()))
                .orElse(AuthorityUtils.NO_AUTHORITIES);
    }

    /**
     * 账号是否未过期。
     * <p>
     * 对安全性要求较高的系统，可能会选择实现账号过期的策略。
     * <p>
     * 比如给用户的账号设置半年期限，如果到期则不能访问系统，除非申请账号延期。到期时，必须立即注销所有在线会话。
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账号是否未锁定。
     * <p>
     * 锁定分为自动锁定和手动锁定两种。
     * <p>
     * 自动锁定：一般是登录时短时间内多次密码错误，将自动锁定一段时间，不再进行密码验证，等下次登录成功自动解锁。
     * <p>
     * 手动锁定：由管理员手动锁定账号，必须手动解锁才能恢复正常。这是一个额外的锁定状态字段，被锁定的账号必须立即注销所有在线会话。
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
     * 因此，如果检测到密码过期，则在下次登录成功后，要求用户必须更改密码。
     * <p>
     * 更改密码一般会导致关联的所有应用失效，特别是在以当前账号系统作为认证源的情况下，此时需要权衡利弊，以决定是否启用该机制。
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否启用。
     * <p>
     * 在安全性较高的系统中，账号由申请流程自动创建，或由管理员手动创建，此时账号属于未启用状态。
     * <p>
     * 为激活此账号，必须在下发的邮件通知中，点击激活链接进行启用。
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
