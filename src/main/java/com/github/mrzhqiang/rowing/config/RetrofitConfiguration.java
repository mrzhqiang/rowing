package com.github.mrzhqiang.rowing.config;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Retrofit 配置。
 */
@Configuration
public class RetrofitConfiguration {

    /**
     * BaseUrl 占位符。
     * <p>
     * {@link retrofit2.http.Url @Url} 注解可以重新定义访问网址，所以这里只是占位符，不具备实际意义。
     * <p>
     * 另外，也可以通过 newBuilder 重新构建 Retrofit 实例，但底层依然共享 OkHttp 相关配置。
     */
    private static final String BASE_URL_HOLDER = "http://localhost";

    /**
     * Retrofit Restful 客户端。
     */
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

}
