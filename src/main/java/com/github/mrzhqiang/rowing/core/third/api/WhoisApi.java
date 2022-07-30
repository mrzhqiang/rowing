package com.github.mrzhqiang.rowing.core.third.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

@SuppressWarnings("HttpUrlsUsage")
public interface WhoisApi {

    String BASE_URL = "http://whois.pconline.com.cn";

    @GET("/ipJson.jsp")
    Observable<WhoIsIpData> ipJson(@Query("ip") String ip, @Query("json") boolean json);
}
