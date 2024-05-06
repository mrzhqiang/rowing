package com.github.mrzhqiang.rowing.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.net.Proxy;
import java.nio.file.Paths;

/**
 * OkHttp 配置。
 */
@Slf4j(topic = "okhttp3")
@Configuration
@RequiredArgsConstructor
public class OkHttpConfiguration {

    private final OkHttpProperties properties;

    /**
     * OkHttp 客户端。
     */
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

    @Bean
    @ConditionalOnProperty(prefix = "rowing.okhttp.proxy", name = "enabled", havingValue = "true")
    public OkHttpClient proxyOkHttpClient(OkHttpClient okHttpClient) {
        OkHttpClient.Builder builder = okHttpClient.newBuilder();
        Proxy proxy = properties.createProxy();
        if (proxy != null) {
            builder.proxy(proxy);
        }
        return builder.build();
    }

    private Cache localCache() {
        File directory = Paths.get(properties.getCachePath()).toFile();
        long maxSize = properties.getCacheMaxSize().toBytes();
        return new Cache(directory, maxSize);
    }

    private Interceptor loggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(log::info);
        interceptor.setLevel(properties.getLoggingLevel());
        return interceptor;
    }

}
