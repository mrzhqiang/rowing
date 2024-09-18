package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.helper.captcha.Captcha;
import com.github.mrzhqiang.helper.captcha.simple.SimpleCaptcha;
import com.github.mrzhqiang.rowing.kaptcha.KaptchaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;

/**
 * 验证码配置。
 */
@Configuration
@EnableConfigurationProperties(KaptchaProperties.class)
public class KaptchaConfiguration {

    @Bean
    public BufferedImageHttpMessageConverter messageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean
    public Captcha captcha() {
        return SimpleCaptcha.of();
    }

}
