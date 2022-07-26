package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.helper.Environments;
import com.github.mrzhqiang.kaptcha.autoconfigure.KaptchaProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Slf4j
@ConditionalOnClass(ThymeleafViewResolver.class)
@EnableConfigurationProperties(WebsiteProperties.class)
@Configuration
public class WebsiteConfiguration implements CommandLineRunner {

    private static final String WEBSITE_KEY = "website";
    private static final String KAPTCHA_KEY = "kaptcha";

    private final WebsiteProperties websiteProperties;
    private final KaptchaProperties kaptchaProperties;
    private final ThymeleafViewResolver resolver;

    public WebsiteConfiguration(WebsiteProperties websiteProperties,
                                KaptchaProperties kaptchaProperties,
                                @Qualifier("thymeleafViewResolver") ThymeleafViewResolver resolver) {
        this.websiteProperties = websiteProperties;
        this.kaptchaProperties = kaptchaProperties;
        this.resolver = resolver;
    }

    @Override
    public void run(String... args) {
        if (Environments.debug()) {
            resolver.addStaticVariable("debug", "true");
        }
        log.info("add website properties to thymeleaf view resolver static variable.");
        resolver.addStaticVariable(WEBSITE_KEY, websiteProperties);
        log.info("add kaptcha properties to thymeleaf view resolver static variable.");
        resolver.addStaticVariable(KAPTCHA_KEY, kaptchaProperties);
    }
}
