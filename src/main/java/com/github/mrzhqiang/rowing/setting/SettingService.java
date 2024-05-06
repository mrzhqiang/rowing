package com.github.mrzhqiang.rowing.setting;

import java.util.Optional;

/**
 * 设置服务。
 */
public interface SettingService {

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
