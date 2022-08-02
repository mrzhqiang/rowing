package com.github.mrzhqiang.rowing.api.system.exception;

import lombok.Data;

/**
 * 异常数据。
 * <p>
 * 用于错误页面的异常消息展示，以及 API 接口的异常消息 JSON 串返回。
 */
@Data
public class ExceptionLogData {

    /**
     * HTTP 状态码。
     */
    private int status;
    /**
     * HTTP 直接原因。
     * <p>
     * 跟状态码一一对应。
     */
    private String error;
    /**
     * 请求方法。
     */
    private String method;
    /**
     * 请求 URL。
     */
    private String url;
    /**
     * 请求参数。
     */
    private String query;
    /**
     * 网络地址。
     * <p>
     * 实际上是发起请求的客户端地址。
     */
    private String address;
    /**
     * 会话 ID。
     */
    private String sessionId;
    /**
     * 异常消息。
     * <p>
     * 抛出异常的消息内容。
     */
    private String message;
    /**
     * 异常痕迹。
     */
    private String trace;
    /**
     * 时间戳。
     * <p>
     * 异常发生时，记录中的数据为 UTC 时间戳，而这里通常是本地时间。
     */
    private String timestamp;
}
