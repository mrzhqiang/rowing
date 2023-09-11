package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.AuditableExcerpt;
import com.github.mrzhqiang.rowing.user.UserForm;
import org.springframework.data.rest.core.config.Projection;

/**
 * 账户表单。
 */
@Projection(name = "account-form", types = {Account.class})
public interface AccountForm extends AuditableExcerpt {

    String getUsername();

    String getPassword();

    String getType();

    String getExpired();

    Integer getFailedCount();

    Boolean getLocked();

    String getPasswordExpired();

    Boolean getDisabled();

    UserForm getUser();

}
