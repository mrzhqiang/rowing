package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.rowing.third.WhoisApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

/**
 * 第三方接口配置。
 */
@Configuration
public class ThirdApiConfiguration {

    /**
     * 网络解析 IP 地理位置。
     */
    @Bean
    public WhoisApi whoisApi(Retrofit retrofit) {
        return retrofit.newBuilder()
                .baseUrl(WhoisApi.BASE_URL)
                .build()
                .create(WhoisApi.class);
    }

}
