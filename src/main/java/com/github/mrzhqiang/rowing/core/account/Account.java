package com.github.mrzhqiang.rowing.core.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.mrzhqiang.rowing.core.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.core.domain.Authority;
import lombok.Getter;
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
@Entity
public class Account extends AuditableEntity implements UserDetails {

    private static final long serialVersionUID = 5162282743335524644L;

    public static final String STUDENT_ID_PREFIX = "sid_";
    public static final String TEACHER_ID_PREFIX = "tid_";

    /**
     * 用户名。
     * <p>
     * 1. 注册创建。校验规则：最小长度 4 位，最大长度 24 位，必须为全小写字母，且以字母开头，字母或数据结尾。
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
    @Column(updatable = false, unique = true, nullable = false)
    private String username;
    /**
     * 密码。
     * <p>
     * 创建时指定，加密存储。
     * <p>
     * 密码绝对不允许明文存储，通过 Spring Security Crypto 的 PasswordEncoder 类进行随机盐编码处理。
     */
    @JsonIgnore
    @ToString.Exclude
    @Column(nullable = false)
    private String password;
    /**
     * 账号所属角色。
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Authority authority;
    /**
     * 首次登录失败时间戳。
     * <p>
     * 指定时间区间（管理后台设定）内的首次登录失败时间戳
     * <p>
     * 记录首次失败时间，可以在一定持续时间内，统计认证失败的次数，在达到最大次数时，锁定账号一段时间，避免被暴力破解。
     */
    private Instant firstFailed;
    /**
     * 失败次数统计。
     * <p>
     * 指定时间区间（管理后台设定）内的失败次数统计
     */
    private int failedCount;
    /**
     * 锁定时间戳。
     * <p>
     * 如果时间戳存在且处于未来的时间点，则说明账号被锁定。
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
     */
    @OneToOne
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
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(authority)
                .map(it -> AuthorityUtils.createAuthorityList(it.name()))
                .orElse(AuthorityUtils.NO_AUTHORITIES);
    }

    /**
     * 判断是否为管理员角色。
     *
     * @return 返回 true 表示当前账户为管理员；否则不是管理员。
     */
    public boolean isAdmin() {
        return Optional.ofNullable(authority)
                .map(Authority.ROLE_ADMIN::equals)
                .orElse(false);
    }

    /**
     * 账号是否未过期。
     * <p>
     * 对安全性要求较高的系统，可能会选择实现账号过期的策略。
     * <p>
     * 比如给用户的账号设置半年期限，如果到期则不能访问系统，除非申请账号延期。到期时，必须立即注销所有在线会话。
     */
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
    @Override
    public boolean isEnabled() {
        return !disabled;
    }
}
