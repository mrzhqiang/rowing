package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.helper.Joiners;
import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置。
 */
@EnableCaching
@Configuration
public class CacheConfiguration extends CachingConfigurerSupport {

    /**
     * 缓存 key 生成器。
     * <p>
     * 如果缓存注解没有指定 key 名称，则使用这个来生成 key 名称。
     * <p>
     * 如果缓存注解指定了 key 名称，则使用指定的名称，此生成器不起作用。
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        // classLowerHyphen -- CacheConfiguration >> cache-configuration
        // methodLowerHyphen -- keyGenerator >> key-generator
        // key -- classLowerHyphen:methodLowerHyphen:params...
        return (target, method, params) -> {
            String className = target.getClass().getSimpleName();
            String classLowerHyphen = UPPER_CAMEL.to(LOWER_HYPHEN, className);
            String methodName = method.getName();
            String methodLowerHyphen = LOWER_CAMEL.to(LOWER_HYPHEN, methodName);
            return Joiners.CACHE.join(classLowerHyphen, methodLowerHyphen, params);
        };
    }
}
