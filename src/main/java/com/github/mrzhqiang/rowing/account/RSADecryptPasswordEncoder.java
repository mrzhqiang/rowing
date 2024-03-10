package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
import com.github.mrzhqiang.rowing.setting.Settings;
import com.github.mrzhqiang.rowing.util.RSADecrypts;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 支持 RSA 解密的密码编码器。
 * <p>
 * 前端登录之后，浏览器会保留表单参数，如果密码是明文的话，很容易泄露出去，因此需要前端对密码进行加密，由后端来解密密码。
 */
public class RSADecryptPasswordEncoder implements PasswordEncoder {

    private final SettingService settingService;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public RSADecryptPasswordEncoder(SettingService settingService) {
        this.settingService = settingService;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @RunAsSystem
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String passwordString = rawPassword.toString();
        // TODO 缓存设置，提升性能
        String password = settingService.findByCode(Settings.PASSWORD_RSA_PRIVATE_KEY)
                .map(Setting::getContent)
                .map(RSADecrypts::parsePrivateKey)
                .map(it -> RSADecrypts.decrypt(passwordString, it))
                .orElse(passwordString);
        return passwordEncoder.matches(password, encodedPassword);
    }

}
