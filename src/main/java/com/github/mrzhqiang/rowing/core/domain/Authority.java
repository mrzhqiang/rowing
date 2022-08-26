package com.github.mrzhqiang.rowing.core.domain;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * 权限。
 * <p>
 * 基于 Spring Security 的角色体系，由于前后端分离，因此这里需要保持足够简单。
 */
public enum Authority {

    /**
     * 管理员。
     * <p>
     * 包含完整的 CURD 权限，以及对一些特殊接口的访问权限，比如账户管理、权限管理等。
     */
    ROLE_ADMIN,
    /**
     * 普通用户。
     * <p>
     * 包含 CURD 中的新增、修改和查询权限，其中查询和修改权限仅限于自己创建的数据。
     */
    ROLE_USER,
    /**
     * 匿名用户。
     * <p>
     * 支持注册、登录相关的接口访问权限。
     */
    ROLE_ANONYMOUS,
    ;

    /**
     * 等级制度。
     * <p>
     * 表示某一角色可以继承对应角色列表的所有权限，比如主管也是员工，同样可以拥有员工的所有权限。
     * <p>
     * 管理员也应该拥有普通用户及匿名用户的所有权限。
     * <p>
     * 注意：新增角色，必须维护好等级制度。
     *
     * @return 等级制度 Map 映射关系。
     */
    public static Map<String, List<String>> hierarchy() {
        Map<String, List<String>> map = Maps.newHashMap();
        // ADMIN 继承 USER 角色的所有权限，包括 USER 角色继承的其他角色的所有权限
        map.put(ROLE_ADMIN.name(), Lists.newArrayList(ROLE_USER.name()));
        // ANONYMOUS 可以访问控制的内容，USER 也可以访问和控制
        map.put(ROLE_USER.name(), Lists.newArrayList(ROLE_ANONYMOUS.name()));
        return map;
    }
}
