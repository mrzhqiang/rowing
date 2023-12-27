package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.util.Authentications;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 账户工具。
 * <p>
 */
public final class Accounts {
    private Accounts() {
        // no instances.
    }

    /**
     * 用户名参数名称。
     */
    public static final String USERNAME_KEY = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;
    /**
     * 密码参数名称。
     */
    @SuppressWarnings("unused")
    public static final String PASSWORD_KEY = UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
    /**
     * 保存管理员初始化密码的文件。
     */
    public static final String ADMIN_PASSWORD_FILENAME = "admin.txt";

    private static final List<String> INTERNAL_USERNAMES = ImmutableList.of(
            Authentications.SYSTEM_USERNAME,
            Authentications.ADMIN_USERNAME
    );

    public static void valid(Account account) {
        Preconditions.checkNotNull(account, "account == null");
        String username = account.getUsername();
        validUsername(username);
    }

    public static void validUsername(String username) {
        Preconditions.checkArgument(StringUtils.hasText(username), "username must be not empty!");
        Preconditions.checkArgument(!INTERNAL_USERNAMES.contains(username.toLowerCase()),
                I18nHolder.getAccessor().getMessage("AccountService.register.invalidUsername",
                        "无效的用户名"));
    }

}
