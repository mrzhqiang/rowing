package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.helper.Environments;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import jakarta.annotation.Nullable;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;

/**
 * Okhttp 属性。
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("rowing.okhttp")
public class OkHttpProperties {

    /**
     * 默认的缓存路径。
     * <p>
     * 这里是在项目运行目录下，指定 .cache 目录作为缓存路径。
     */
    private final static String DEF_CACHE_PATH = Paths.get(Environments.USER_DIR, ".cache").toString();
    /**
     * 默认的缓存上限大小。
     * <p>
     * 这里指定为 1GB 大小。
     */
    private final static DataSize DEF_CACHE_MAX_SIZE = DataSize.ofGigabytes(1);
    /**
     * 默认的调用超时时长。
     * <p>
     * 这里指定为 5s 时长。
     */
    private static final Duration DEF_CALL_TIMEOUT = Duration.ofSeconds(5);

    /**
     * OKHttp 的缓存路径。
     */
    private String cachePath = DEF_CACHE_PATH;
    /**
     * OkHttp 的缓存最大值。
     */
    private DataSize cacheMaxSize = DEF_CACHE_MAX_SIZE;
    /**
     * OkHttp 的调用超时。
     * <p>
     * 调用超时跨越整个调用：解析 DNS、连接、写入请求正文、服务器处理和读取响应正文。
     */
    private Duration callTimeout = DEF_CALL_TIMEOUT;
    /**
     * OkHttp 的日志等级。
     * <p>
     * NONE 性能最好，但不输出任何信息。
     * <p>
     * BASIC 只打印请求和响应信息。
     * <p>
     * BODY 输出完整的请求内容和响应体。
     * <p>
     * 常用的就是这三个，具体细节请参考其 API 描述。
     */
    private HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;
    /**
     * 代理。
     */
    private OkhttpProxy proxy = new OkhttpProxy();

    /**
     * 创建代理实例。
     *
     * @return 代理实例。
     */
    @Nullable
    public Proxy createProxy() {
        return Optional.ofNullable(proxy)
                .filter(OkhttpProxy::getEnabled)
                .map(it -> new Proxy(it.getType(), InetSocketAddress.createUnresolved(it.getHost(), it.getPort())))
                .orElse(null);
    }

    /**
     * OkHttp 代理。
     */
    @Setter
    @Getter
    public static class OkhttpProxy {

        /**
         * 是否开启代理。
         * <p>
         * 主要是提供给配置类去生成相关实例。
         */
        private Boolean enabled = false;
        /**
         * 代理类型。
         */
        private Proxy.Type type;
        /**
         * 代理主机地址。
         */
        private String host;
        /**
         * 代理端口。
         */
        private Integer port;

    }

}
