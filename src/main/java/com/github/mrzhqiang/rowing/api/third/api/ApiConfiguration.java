package com.github.mrzhqiang.rowing.api.third.api;

import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.File;
import java.nio.file.Paths;

@Slf4j
@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class ApiConfiguration {

    /**
     * BaseUrl 占位符。
     * <p>
     * {@link retrofit2.http.Url @Url} 注解可以重新定义访问网址，所以这里只是占位符，不具备实际意义。
     * <p>
     * 另外，也可以通过 newBuilder 重新构建 Retrofit 实例，但底层依然共享 OkHttp 相关配置。
     */
    private static final String BASE_URL_HOLDER = "http://localhost";

    private final ApiProperties properties;

    public ApiConfiguration(ApiProperties properties) {
        this.properties = properties;
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                // 调用超时跨越整个调用：解析 DNS、连接、写入请求正文、服务器处理和读取响应正文。
                .callTimeout(properties.getCallTimeout())
                .cache(localCache())
                .followSslRedirects(false)
                .followRedirects(false)
                .addInterceptor(loggingInterceptor())
                .build();
    }

    @Bean
    public Retrofit retrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL_HOLDER)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    private Cache localCache() {
        File directory = Paths.get(properties.getCachePath()).toFile();
        long maxSize = properties.getCacheMaxSize().toBytes();
        return new Cache(directory, maxSize);
    }

    private Interceptor loggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(log::info);
        loggingInterceptor.setLevel(properties.getLoggingLevel());
        return loggingInterceptor;
    }
}
