package com.github.mrzhqiang.rowing.setting;

import org.springframework.util.ResourceUtils;

import java.time.Duration;
import java.util.Optional;

/**
 * 设置服务。
 */
public interface SettingService {

    /**
     * 系统设置 Excel 文件地址。
     */
    String EXCEL_FILE_LOCATION = ResourceUtils.CLASSPATH_URL_PREFIX + "data/setting.xlsx";
    /**
     * 默认设置在 Excel 文件中的 Sheet 页面名称。
     */
    String SHEET_NAME = "data";

    /**
     * 最大登录失败次数的设置名称。
     */
    String MAX_LOGIN_FAILED = "maxLoginFailed";
    /**
     * 默认最大登录失败次数。
     */
    int DEF_MAX_LOGIN_FAILED = 5;
    /**
     * 账户锁定持续时长。
     */
    String ACCOUNT_LOCKED_DURATION = "accountLockedDuration";
    /**
     * 默认账户锁定持续时长。
     */
    Duration DEF_ACCOUNT_LOCKED_DURATION = Duration.ofMinutes(30);
    /**
     * 密码传输时的 RSA 私钥。
     */
    String PASSWORD_RSA_PRIVATE_KEY = "passwordRsaPrivateKey";
    /**
     * 密码传输时的 RSA 公钥。
     */
    @SuppressWarnings("unused")
    String PASSWORD_RSA_PUBLIC_KEY = "passwordRsaPublicKey";
    /**
     * 管理员电子邮箱。
     */
    String ADMIN_EMAIL = "adminEmail";
    /**
     * 管理员电话号码。
     */
    String ADMIN_PHONE_NUMBER = "adminPhoneNumber";

    /**
     * 初始化系统设置。
     */
    void init();

    /**
     * 通过设置代码找到可选的设置。
     *
     * @param code 代码。
     * @return 可选的设置。
     */
    Optional<Setting> findByCode(String code);

    RSAKeyData createRsaKey();

}
