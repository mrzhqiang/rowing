package com.github.mrzhqiang.rowing.third;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Okhttp 配置。
 * <p>
 * 主要定义 OkhttpClient 的参数，并作为 Bean 提供给 Spring 容器。
 */
@EnableConfigurationProperties(OkHttpProperties.class)
@Configuration
public class OkHttpConfiguration {

    private final OkHttpProperties properties;

    public OkHttpConfiguration(OkHttpProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .callTimeout(properties.getCallTimeout())
                .cache(localCache())
                .followSslRedirects(false)
                .followRedirects(false)
                .addInterceptor(loggingInterceptor())
                .build();
    }

    private Cache localCache() {
        File directory = Paths.get(properties.getCachePath()).toFile();
        long maxSize = properties.getCacheMaxSize().toBytes();
        return new Cache(directory, maxSize);
    }

    private Interceptor loggingInterceptor() {
        Logger logger = Logger.getLogger("okhttp3");
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(logger::info);
        interceptor.setLevel(properties.getLoggingLevel());
        return interceptor;
    }
}
