package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.rowing.third.WhoisApi;
import com.google.common.collect.Lists;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import retrofit2.Retrofit;

/**
 * IP 地理位置配置。
 * <p>
 * 在系统中，主要是将 IP 地址转换地理空间位置，需要用到本地数据库查询和网络 API 查询。
 */
@Configuration
public class IpLocationConfiguration {

    /**
     * 本地数据库文件。
     */
    public static final String DB_FILE = ResourceUtils.CLASSPATH_URL_PREFIX + "data/GeoLite2-City.mmdb";

    /**
     * GEO 数据库读取器。
     */
    @Bean
    public DatabaseReader geoDatabaseReader(@Value(DB_FILE) Resource geoResource) throws Exception {
        return new DatabaseReader.Builder(geoResource.getInputStream())
                // 需要按照喜欢程度排序的语言列表，从最喜欢到最不喜欢
                .locales(Lists.newArrayList("zh_CN", "en"))
                // CHM 即 ConcurrentHashMap，表示使用内存缓存数据，重启后丢失
                // 这个缓存有存储上限，一旦达到上限值，新的数据将只从数据库读取，不走缓存
                // 可以通过构造器传参修改存储上限，这里我们先用默认值 4096 观察，不满足需求，再通过配置文件传参到这里进行修改
                // 注意：可用内存决定了缓存空间的大小，应该设置一个开关来保证低配服务器不启用缓存
                // 或重写缓存节点的实现，改为中间件缓存，比如 Redis 内存数据库
                .withCache(new CHMCache())
                .build();
    }

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
