package com.github.mrzhqiang.rowing.role;

import com.github.mrzhqiang.rowing.account.AccountExcerpt;
import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import com.github.mrzhqiang.rowing.menu.MenuExcerpt;
import com.github.mrzhqiang.rowing.menu.MenuResourceExcerpt;
import org.springframework.data.rest.core.config.Projection;

import java.util.List;

/**
 * 角色表单。
 * <p>
 */
@Projection(name = "role-form", types = {Role.class})
public interface RoleForm extends AuditableProjection {

    String getName();

    String getCode();

    Boolean getImmutable();

    List<AccountExcerpt> getAccounts();

    List<MenuExcerpt> getMenus();

    List<MenuResourceExcerpt> getMenuResources();

}
