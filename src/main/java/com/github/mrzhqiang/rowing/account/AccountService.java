package com.github.mrzhqiang.rowing.account;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * 账户服务。
 */
public interface AccountService extends UserDetailsService {

    /**
     * 通过用户名加载用户信息。
     * <p>
     * 这个方法在认证时调用，也就是说，登录时会调用这个方法。
     * <p>
     * 注意：为了保证在 {@link org.springframework.security.core.Authentication} 中的纯粹性，我们返回 {@link User} 实例。
     * <p>
     * 如果需要通过用户名找到账户，请使用 {@link #findByUsername(String)} 方法。
     *
     * @param username 用户名。
     * @return 用户信息。
     * @throws UsernameNotFoundException 当无法通过用户名找到用户时，抛出此异常，表示登录失败。
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 通过用户名找到账户。
     *
     * @param username 用户名
     * @return 可选的账户。
     */
    Optional<Account> findByUsername(String username);

    /**
     * 初始化超级管理员账户。
     */
    void initAdmin();

    /**
     * 注册系统账户。
     *
     * @param form 注册表单。
     */
    void register(RegisterForm form);

    /**
     * 注册学生账户。
     * <p>
     * 根据 {@link Account} 用户名的相关规则进行注册。
     *
     * @param form 学生信息表单。
     * @return 可选的系统账户。如果存在表示注册成功；否则表示注册失败。
     */
    Optional<Account> register(StudentInfoForm form);

    /**
     * 注册教师账户。
     * <p>
     * 根据 {@link Account} 用户名的相关规则进行注册。
     *
     * @param form 教师信息表单。
     * @return 可选的系统账户。如果存在表示注册成功；否则表示注册失败。
     */
    Optional<Account> register(TeacherInfoForm form);

    void update(Account account);

}
