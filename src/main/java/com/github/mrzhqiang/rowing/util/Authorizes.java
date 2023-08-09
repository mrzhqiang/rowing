package com.github.mrzhqiang.rowing.util;

import com.github.mrzhqiang.rowing.domain.AccountType;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 授权工具。
 * <p>
 * 主要提供简便的授权表达式，避免编写错误带来的异常。
 * <p>
 * 使用方式，在注解中添加以下字符串：
 * <p>
 * "hasAuthority('xxx')"
 * <p>
 * "hasRole('ROLE_xxx')" or "hasRole('xxx')"
 * <p>
 * "permitAll()"
 * <p>
 * "denyAll()"
 * <p>
 * "isAnonymous()"
 * <p>
 * "isAuthenticated()"
 * <p>
 * "isRememberMe()"
 * <p>
 * "isFullyAuthenticated()"
 * <p>
 * "hasPermission(#target, 'read')" or "hasPermission(#target, 'admin')"
 */
public final class Authorizes {
    private Authorizes() {
        // no instances
    }

    /**
     * 管理员角色授权。
     */
    public static final String HAS_AUTHORITY_ADMIN = "hasAuthority('ADMIN')";
    /**
     * 用户角色授权。
     */
    public static final String HAS_AUTHORITY_USER = "hasAuthority('USER')";
    /**
     * 游客角色授权。
     */
    public static final String HAS_AUTHORITY_ANONYMOUS = "hasAuthority('ANONYMOUS')";

    /**
     * 等级制度。
     * <p>
     * 表示某一授权可以继承其他授权的所有权限，比方说经理也是员工，同样可以拥有员工的所有权限。
     * <p>
     * 因此，管理员可以包含非管理员的所有权限。
     *
     * @return 等级制度 Map 映射关系。
     */
    public static Map<String, List<String>> hierarchy() {
        Map<String, List<String>> map = Maps.newHashMap();
        // USER 包含 ANONYMOUS 可以访问控制的内容
        map.put(AccountType.USER.name(), Lists.newArrayList(AccountType.ANONYMOUS.name()));
        // ADMIN 继承除自身之外的所有权限
        map.put(AccountType.ADMIN.name(), Arrays.stream(AccountType.values())
                .filter(it -> !AccountType.ADMIN.equals(it))
                .map(Enum::name)
                .collect(Collectors.toList()));
        return map;
    }

}
