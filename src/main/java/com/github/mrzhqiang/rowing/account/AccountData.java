package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.user.User;
import org.springframework.data.rest.core.config.Projection;

/**
 * 账户数据。
 * <p>
 * 账户具有私密性，所以利用投影功能，屏蔽隐私相关字段。
 */
@Projection(name = "account", types = {Account.class})
public interface AccountData {

    String getUsername();

    User getUser();

}
