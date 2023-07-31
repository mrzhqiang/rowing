package com.github.mrzhqiang.rowing.setting;

import org.springframework.util.ResourceUtils;

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
    String MAX_LOGIN_FAILED = "max-login-failed";
    /**
     * 默认最大登录失败次数。
     */
    int DEF_MAX_LOGIN_FAILED = 5;
    /**
     * 密码传输时的 RSA 私钥。
     */
    String PASSWORD_RSA_PRIVATE_KEY = "password-rsa-private-key";
    /**
     * 密码传输时的 RSA 公钥。
     */
    String PASSWORD_RSA_PUBLIC_KEY = "password-rsa-public-key";

    /**
     * 初始化系统设置。
     */
    void init();

    /**
     * 通过设置名称找到设置。
     *
     * @param name 名称。
     * @return 可选的设置实例。
     */
    Optional<Setting> findByName(String name);

    RSAKeyData createRsaKey();
}
