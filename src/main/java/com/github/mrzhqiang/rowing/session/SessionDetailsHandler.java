package com.github.mrzhqiang.rowing.session;

import com.github.mrzhqiang.rowing.third.WhoisApi;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.reactivex.Single;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

/**
 * 会话详情处理器。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SessionDetailsHandler {

    private final WhoisApi api;
    private final DatabaseReader reader;
    private final SessionDetailsMapper mapper;

    @Timed
    @Counted
    public SessionDetails findBy(String ip) {
        return api.ipJson(ip, true)
                .timeout(3, TimeUnit.SECONDS)
                .map(mapper::toDetails)
                .onErrorResumeNext(Single.just(ip)
                        .timeout(2, TimeUnit.SECONDS)
                        .map(this::toDetails))
                .blockingGet();

    }

    private SessionDetails toDetails(String ip) throws IOException, GeoIp2Exception {
        return mapper.toDetails(reader.city(InetAddress.getByName(ip)));
    }

}
