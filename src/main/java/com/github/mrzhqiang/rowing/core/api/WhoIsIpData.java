package com.github.mrzhqiang.rowing.core.api;

import lombok.Data;

/**
 * IP 相关数据。
 */
@Data
public class WhoIsIpData {

    /**
     * 当前 IP 地址。
     */
    private String ip;
    /**
     * 省份。
     */
    private String pro;
    /**
     * 省份代码。
     */
    private String proCode;
    /**
     * 城市。
     */
    private String city;
    /**
     * 城市代码。
     */
    private String cityCode;
    /**
     * 区县。
     */
    private String region;
    /**
     * 区县代码。
     */
    private String regionCode;
    /**
     * 地址 + 运营商。
     */
    private String addr;
    /**
     * 区域名称汇总？
     */
    private String regionNames;
    /**
     * 出错信息。
     */
    private String err;
}
