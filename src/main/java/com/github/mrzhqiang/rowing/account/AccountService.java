package com.github.mrzhqiang.rowing.account;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

import java.util.Optional;

/**
 * 账户服务。
 * <p>
 * 用于处理账户相关的业务：
 * <p>
 * 1. 通过用户名加载账户实体。
 * <p>
 * 2. 初始化管理员账户，如果已初始化则什么也不做。
 * <p>
 * 3. 通过登录表单注册系统账户。
 * <p>
 * 4. 通过学号注册系统账户。
 */
public interface AccountService extends UserDetailsService {

    /**
     * 管理员的用户名。
     */
    String ADMIN_USERNAME = "admin";
    /**
     * 保存管理员初始化密码的文件。
     */
    String PASSWORD_FILE = "admin.txt";
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
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * 初始化管理员的系统账户。
     */
    Account initAdmin();

    /**
     * 注册系统账户。
     *
     * @param form 注册表单。
     * @return 可选的系统账户。如果存在表示注册成功；否则表示注册失败。
     */
    Optional<Account> register(RegisterForm form);

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
    Optional<Account> registerForTeacher(TeacherInfoForm form);
}
