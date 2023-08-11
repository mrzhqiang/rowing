package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.rowing.third.WhoisApi;
import com.google.common.collect.Lists;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.util.ResourceUtils;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * 系统配置。
 * <p>
 * 整合自定义属性及配置。
 */
@EnableConfigurationProperties({
        OkHttpProperties.class,
        DictProperties.class,
        MenuProperties.class,
})
@EnableRedisHttpSession
@Configuration
public class RowingConfiguration {

    private final OkHttpProperties properties;

    public RowingConfiguration(OkHttpProperties properties) {
        this.properties = properties;
    }

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

    /**
     * 本地数据库文件。
     */
    private static final String DB_FILE = ResourceUtils.CLASSPATH_URL_PREFIX + "data/GeoLite2-City.mmdb";

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
