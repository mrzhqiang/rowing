package com.github.mrzhqiang.rowing.role;

import org.springframework.data.rest.core.config.Projection;

/**
 * 角色投影。
 * <p>
 */
@Projection(name = "role-excerpt", types = {Role.class})
public interface RoleExcerpt {

    String getCode();

}
