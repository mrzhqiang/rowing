package com.github.mrzhqiang.rowing.core.account;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

/**
 * 账号服务。
 * <p>
 * 用于处理账号相关的业务。
 */
public interface AccountService extends UserDetailsService {

    /**
     * 管理员的用户名。
     */
    String ADMIN_USERNAME = "admin";

    /**
     * 用户名参数名称。
     */
    String USERNAME_KEY = SPRING_SECURITY_FORM_USERNAME_KEY;
    /**
     * 密码参数名称。
     */
    String PASSWORD_KEY = SPRING_SECURITY_FORM_PASSWORD_KEY;

    /**
     * 通过用户名加载账户。
     *
     * @param username 用户名。
     * @return 账户实例。
     * @throws UsernameNotFoundException 当无法通过用户名找到账号时，抛出此异常，表示登录失败。
     */
    @Override
    Account loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 初始化管理员账号。
     */
    Account initAdmin();

    /**
     * 注册账号。
     *
     * @param form 注册表单。
     * @return 返回 True 表示注册成功；否则注册失败。
     */
    boolean register(RegisterForm form);
}
