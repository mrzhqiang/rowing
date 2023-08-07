package com.github.mrzhqiang.rowing.domain;

import com.github.mrzhqiang.rowing.util.Authorizes;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 授权。
 * <p>
 * 基于 Spring Security 体系构建授权。
 * <p>
 * 对于角色，以 ROLE_ 作为前缀，目前只有三种角色：管理员、用户、匿名人员。
 * <p>
 * 对于权限，以 AUTH_ 作为前缀，中间是仓库名称，并以方法名称结尾，保证唯一性。
 * <p>
 * 角色用于功能授权：公共资源--匿名人员，系统功能--用户，管理后台--管理员。基于等级制度，可以让后者拥有前者的所有权限。
 * <p>
 * 权限用于接口授权：通常是执行方法之前，进行预授权，只有拥有相关权限标识符，才能执行成功，避免越权操作。
 * <p>
 * 权限相对角色来说，粒度更细，方式更灵活，需要搭配用户以及菜单来实现，对于账户授权来说，只需要关注角色即可。
 * <p>
 * 注意：枚举值需改为大写字母，如果存在驼峰命名，则通过 _ 符号分离。
 */
public enum Authority {

    /**
     * 管理员。
     */
    ROLE_ADMIN,
    /**
     * 用户。
     */
    ROLE_USER,
    /**
     * 匿名人员。
     */
    ROLE_ANONYMOUS,
    ;

    /**
     * 通过字符串转换权限枚举。
     *
     * @param value 字符串。
     * @return 权限枚举。
     */
    public static Authority of(String value) {
        if (!StringUtils.hasText(value)) {
            return ROLE_ANONYMOUS;
        }

        return Stream.of(values())
                .filter(it -> value.equals(it.name()
                        .replaceFirst(Authorizes.ROLE_PREFIX, "")
                        .replaceFirst(Authorizes.AUTH_PREFIX, "")))
                .findFirst()
                .orElse(ROLE_ANONYMOUS);
    }

    /**
     * 等级制度。
     * <p>
     * 表示某一角色可以继承对应角色的所有权限，比方说经理也是员工，同样可以拥有员工的所有权限。
     * <p>
     * 因此，管理员应该继承用户及匿名人员，也就是需要包含非管理员的所有角色和权限。
     * <p>
     * 注意：为了避免复杂，应该保持目前的角色不再新增。对于权限，则可以根据接口进行新增。
     *
     * @return 等级制度 Map 映射关系。
     */
    public static Map<String, List<String>> hierarchy() {
        Map<String, List<String>> map = Maps.newHashMap();
        // ROLE_ANONYMOUS 可以访问控制的内容，ROLE_USER 也可以访问和控制
        map.put(ROLE_USER.name(), Lists.newArrayList(ROLE_ANONYMOUS.name()));
        // ROLE_ADMIN 继承除自身之外的所有权限
        map.put(ROLE_ADMIN.name(), Arrays.stream(values())
                .filter(it -> !ROLE_ADMIN.equals(it))
                .map(Enum::name)
                .collect(Collectors.toList()));
        return map;
    }
}
