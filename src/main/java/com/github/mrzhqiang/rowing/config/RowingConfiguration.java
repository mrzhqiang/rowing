package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.rowing.third.WhoisApi;
import com.google.common.collect.Lists;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import retrofit2.Retrofit;

/**
 * 系统配置。
 * <p>
 * 整合自定义属性及配置。
 */
@EnableConfigurationProperties({
        DictProperties.class,
        MenuProperties.class,
})
@Configuration
public class RowingConfiguration {

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
