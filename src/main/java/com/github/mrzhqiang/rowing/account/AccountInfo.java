package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 账户信息。
 * <p>
 * 账户具有私密性，所以利用投影功能，屏蔽隐私相关字段。
 */
@Projection(name = "account-info", types = {Account.class})
public interface AccountInfo extends BaseProjection {

    String getUsername();

    @Value("#{target.user?.nickname}")
    String getNickname();

}
