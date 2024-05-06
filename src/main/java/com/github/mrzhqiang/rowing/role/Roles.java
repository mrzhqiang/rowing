package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.menu.MenuResource;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 角色工具。
 */
@UtilityClass
public class Roles {

    /**
     * 通过角色集合找到对应的授权列表。
     *
     * @param roles 角色集合。
     * @return 授权列表。
     */
    public static List<GrantedAuthority> findAuthorities(Collection<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }

        return roles.stream()
                .flatMap(Roles::concatRoleAndMenuResources)
                .distinct()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private static Stream<String> concatRoleAndMenuResources(Role role) {
        return Stream.concat(Stream.of(role.getCode()), role.getMenuResources().stream().map(MenuResource::getCode));
    }

    /**
     * 通过角色集合找到对应的代码列表。
     *
     * @param roles 角色集合。
     * @return 代码列表。
     */
    public static List<String> findCodes(Collection<Role> roles) {
        if (CollectionUtils.isEmpty(roles)) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(Role::getCode)
                .collect(Collectors.toList());
    }

}
