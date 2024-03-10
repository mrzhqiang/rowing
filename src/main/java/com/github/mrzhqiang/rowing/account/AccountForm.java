package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import com.github.mrzhqiang.rowing.user.UserForm;
import org.springframework.data.rest.core.config.Projection;

/**
 * 账户表单。
 */
@Projection(name = "account-form", types = {Account.class})
public interface AccountForm extends BaseProjection {

    String getUsername();

    String getPassword();

    String getType();

    String getAuthority();

    String getExpired();

    Integer getFailedCount();

    Boolean getLocked();

    String getPasswordExpired();

    Boolean getDisabled();

    UserForm getUser();

}
