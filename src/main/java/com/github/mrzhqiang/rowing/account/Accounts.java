package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.util.Authentications;
import com.github.mrzhqiang.rowing.util.Ranges;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.experimental.UtilityClass;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.github.mrzhqiang.rowing.domain.Domains.USERNAME_MAX_LENGTH;
import static com.github.mrzhqiang.rowing.domain.Domains.USERNAME_MIN_LENGTH;

/**
 * 账户工具。
 */
@UtilityClass
public class Accounts {

    /**
     * 保存系统管理员初始密码的文件。
     */
    public static final String SYSTEM_ADMIN_PASSWORD_FILENAME = "admin.txt";
    /**
     * 用户名参数的键名。
     */
    public static final String USERNAME_KEY = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;
    /**
     * 密码参数的键名。
     */
    @SuppressWarnings("unused")
    public static final String PASSWORD_KEY = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;

    /**
     * 内部用户名，禁止注册。
     */
    private static final List<String> INTERNAL_USERNAMES = ImmutableList.of(
            Authentications.SYSTEM_USERNAME,
            Authentications.ADMIN_USERNAME
    );

    /**
     * 验证用户名。
     *
     * @param username 用户名。
     */
    public static void validateUsername(String username) {
        // 接口会自动验证空值，这里主要提供内部调用的参数验证
        Preconditions.checkArgument(StringUtils.hasText(username), I18nHolder.getAccessor().getMessage(
                "Accounts.valid.usernameEmpty", "用户名不能为空"));
        // 用户名长度必须在范围内
        boolean validUsernameLength = Ranges.in(username.length(), USERNAME_MIN_LENGTH, USERNAME_MAX_LENGTH);
        Preconditions.checkArgument(validUsernameLength, I18nHolder.getAccessor().getMessage(
                "Accounts.validUsername.invalid", "无效的用户名"));
        // 禁止注册内部用户名
        Preconditions.checkArgument(!INTERNAL_USERNAMES.contains(username), I18nHolder.getAccessor().getMessage(
                "Accounts.valid.usernameInvalid", "无效的用户名"));
    }

    /**
     * 检查账户是否过期。
     *
     * @param account 账户。
     * @return 返回 true 表示账户已过期。
     */
    public static boolean checkExpired(Account account) {
        return Optional.ofNullable(account.getExpired())
                // now > expired 表示账户已过期
                .map(Instant.now()::isAfter)
                .orElse(false);
    }

    /**
     * 检查账户是否处于锁定状态。
     * <p>
     * 优先检查是否强制锁定，如果没有强制锁定，才去检查锁定时间戳。
     *
     * @param account 账户。
     * @return 返回 true 表示账户已锁定。
     */
    public static boolean checkLocked(Account account) {
        // forced locked 表示账户被强制锁定
        return Boolean.TRUE.equals(account.getForcedLocked()) || Optional.ofNullable(account.getLocked())
                // now < locked 表示账户被锁定
                .map(Instant.now()::isBefore)
                .orElse(false);
    }

    /**
     * 检查账户凭据是否过期。
     * <p>
     * 凭据通常是密码，后续如果加入 Token 之类的凭据，也应该在此追加检查逻辑。
     *
     * @param account 账户。
     * @return 返回 true 表示账户凭据已过期。
     */
    public static boolean checkCredentialsExpired(Account account) {
        return Optional.ofNullable(account.getPasswordExpired())
                // now > password expired 表示凭据（密码）已过期
                .map(Instant.now()::isAfter)
                .orElse(false);
    }

}
