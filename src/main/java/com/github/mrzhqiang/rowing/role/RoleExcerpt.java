package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 角色投影。
 * <p>
 */
@Projection(name = "role-excerpt", types = {Role.class})
public interface RoleExcerpt extends AuditableProjection {

    String getName();

    String getCode();

    Boolean getImmutable();

}
