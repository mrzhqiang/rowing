package com.github.mrzhqiang.rowing.module.account;

import lombok.Data;
import org.springframework.data.rest.core.config.Projection;

/**
 * 账户数据。
 * <p>
 * 账户具有私密性，所以利用投影功能，屏蔽隐私相关字段。
 */
@Data
@Projection(types = {Account.class}, name = "account")
public class AccountData {


}
