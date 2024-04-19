package com.github.mrzhqiang.rowing.account;

import org.springframework.security.core.Authentication;

import java.util.Optional;

/**
 * 账户服务。
 */
public interface AccountService {

    /**
     * 初始化账户。
     */
    void init();

    /**
     * 通过用户名找到账户。
     *
     * @param username 用户名
     * @return 可选的账户。
     */
    Optional<Account> findByUsername(String username);

    /**
     * 处理登录成功。
     *
     * @param authentication 认证信息。
     */
    void handleLoginSuccess(Authentication authentication);

    /**
     * 处理登录失败。
     *
     * @param authentication 认证信息。
     */
    void handleLoginFailed(Authentication authentication);

    /**
     * 注册账户。
     *
     * @param form 密码确认表单。
     */
    void register(PasswordConfirmForm form);

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

    /**
     * 更新账户。
     *
     * @param account 账户实体。
     */
    void update(Account account);
}
