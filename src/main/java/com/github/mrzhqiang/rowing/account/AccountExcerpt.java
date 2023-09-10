package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.domain.AuditableExcerpt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 账户摘要。
 */
@Projection(name = "account-excerpt", types = {Account.class})
public interface AccountExcerpt extends AuditableExcerpt {

    String getUsername();

    String getPassword();

    AccountType getType();

    @Value("#{target.user.nickname}")
    String getNickname();

}
