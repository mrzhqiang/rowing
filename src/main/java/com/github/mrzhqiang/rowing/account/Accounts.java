package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.util.Ranges;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import lombok.experimental.UtilityClass;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.github.mrzhqiang.rowing.domain.Domains.USERNAME_MAX_LENGTH;
import static com.github.mrzhqiang.rowing.domain.Domains.USERNAME_MIN_LENGTH;

/**
 * 账户工具。
 */
@UtilityClass
public class Accounts {

    /**
     * 系统虚拟用户名。
     * <p>
     * 系统内部操作不存在用户会话，此时需要系统虚拟用户来避免编写复杂的授权逻辑。
     * <p>
     * 并且，数据库审计也需要使用系统虚拟用户名来占位。
     */
    public static final String SYSTEM_USERNAME = "system";
    /**
     * 超级管理员用户名。
     */
    public static final String ADMIN_USERNAME = "admin";
    /**
     * 保存超级管理员初始密码的文件。
     */
    public static final String ADMIN_PASSWORD_FILENAME = "admin.txt";
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
            SYSTEM_USERNAME,
            ADMIN_USERNAME
    );

    /**
     * 验证用户名的有效性。
     */
    public static void validUsername(String username) {
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

}
