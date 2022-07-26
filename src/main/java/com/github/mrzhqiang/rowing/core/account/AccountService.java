package com.github.mrzhqiang.rowing.core.account;

import com.github.mrzhqiang.helper.random.RandomStrings;
import com.google.common.base.Strings;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

import java.util.Objects;

/**
 * 账号服务。
 * <p>
 * 作为基础服务，被具体模块的账号服务继承，提供通用的接口方法。
 */
public interface AccountService extends UserDetailsService, UserDetailsPasswordService {

    /**
     * UID 前缀最小长度。
     */
    int UID_PREFIX_MIN_LENGTH = 3;
    /**
     * UID 前缀最大长度。
     */
    int UID_PREFIX_MAX_LENGTH = 5;
    /**
     * UID 中缀取模。
     */
    int UID_INFIX_MOD = 1000;
    /**
     * UID 最小长度。
     */
    int UID_MIN_LENGTH = 7;
    /**
     * UID 后缀填充字符。
     */
    char UID_SUFFIX_PAD_CHAR = '0';
    /**
     * UID 参数名称。
     */
    String UID_KEY = "uid";
    /**
     * 用户名参数名称。
     */
    String USERNAME_KEY = SPRING_SECURITY_FORM_USERNAME_KEY;
    /**
     * 密码参数名称。
     */
    String PASSWORD_KEY = SPRING_SECURITY_FORM_PASSWORD_KEY;

    /**
     * 通过用户找到账号。
     *
     * @param user 用户，通常的实现是 {@link org.springframework.security.core.userdetails.User} 类。
     * @return 账号实例，同样也实现了 {@link UserDetails} 接口，但只是方便转换成 {@link org.springframework.security.core.userdetails.User} 类。
     * @throws RuntimeException 当无法通过用户详情找到账号时，抛出此异常，一般是数据库问题。
     */
    Account findByUser(UserDetails user);

    /**
     * 通过生成 UID 的方法。
     *
     * @param username 用户名。
     * @return uid 字符串。
     */
    default String generateUid(String username) {
        // 参考：HashMap.hash(obj) 由高 16 位组成 code 避免高频率的哈希冲撞
        int h;
        int code = (h = Objects.hashCode(username)) ^ (h >>> 16);
        // Math.floorMod 方法返回的数值符号由 y 参数符号决定，因此这里永远不会返回负数，符合预期
        int floorMod = Math.floorMod(code, UID_INFIX_MOD);

        // 生成位数为 min 及 max 之间的随机数字
        String uid = RandomStrings.ofNumber(UID_PREFIX_MIN_LENGTH, UID_PREFIX_MAX_LENGTH);
        uid = uid + floorMod;
        // 保证最小长度为 7 位，不足 7 位尾部补零
        uid = Strings.padEnd(uid, UID_MIN_LENGTH, UID_SUFFIX_PAD_CHAR);
        return uid;
    }

    /**
     * 通过用户名或 uid 加载用户详情。
     *
     * @param usernameOrUid 用户名/uid，用户名必须以字母开头，uid 则是纯数字。
     * @return 用户详情，通常是 {@link org.springframework.security.core.userdetails.User} 类。
     * @throws UsernameNotFoundException 当无法通过用户名找到账号时，抛出此异常，表示登录失败。
     */
    @Override
    UserDetails loadUserByUsername(String usernameOrUid) throws UsernameNotFoundException;

    /**
     * 注册账号。
     *
     * @param accountForm 前端传递过来的账号表单。
     * @return 返回 True 表示注册成功；否则注册失败。
     */
    boolean register(AccountForm accountForm);

    /**
     * 更新密码。
     * <p>
     * Spring Security 框架在认证账号时，如果发现密码强度低于当前的密码加密器，则自动调用此方法去更新密码。
     *
     * @param user        用户详情，由 loadUserByUsername 返回的实例。
     * @param newPassword 新的密码，从登录时提交的表单得到原始密码，再通过加密器进行编码后得到的新密码。
     * @return 新的用户详情。
     */
    @Override
    UserDetails updatePassword(UserDetails user, String newPassword);
}
