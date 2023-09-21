package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 账户穿梭数据。
 */
@Projection(name = "account-transfer", types = {Account.class})
public interface AccountTransfer extends BaseProjection {

    @Value("#{target.id}")
    String getKey();

    @Value("#{target.user.nickname}")
    String getLabel();

    Boolean getDisabled();

}
