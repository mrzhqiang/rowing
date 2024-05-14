package com.github.mrzhqiang.rowing.kaptcha;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Properties;

/**
 * 验证码配置属性。
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("rowing.kaptcha")
public class KaptchaProperties {

    public static final String KEY_SESSION_CODE = "KAPTCHA_SESSION_CODE";
    public static final String KEY_SESSION_DATE = "KAPTCHA_SESSION_DATE";

    public static final String DEF_EMPTY_TIPS = "验证码不能为空";
    public static final String DEF_INVALID_TIPS = "无效的验证码";
    public static final String DEF_TIMEOUT_TIPS = "验证码已过期";

    private static final String DEF_PATH = "/kaptcha";
    private static final String DEF_PARAMETER = "kaptcha";
    private static final Duration DEF_TIMEOUT = Duration.ofMinutes(5);

    private Boolean enabled = false;
    private String path = DEF_PATH;
    private String parameter = DEF_PARAMETER;
    private Duration timeout = DEF_TIMEOUT;
    private Properties config = new Properties();

}
