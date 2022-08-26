package com.github.mrzhqiang.rowing.core.exception;

import com.github.mrzhqiang.rowing.core.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 异常日志。
 * <p>
 * 异常日志通常用于后台管理系统的错误分析。
 * <p>
 * 之所以不用 ELK 是因为首先我们要保证系统足够轻量级，如果系统在未来变得臃肿，也可以通过重构接入 ELK 之类的中间件进行解耦。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ExceptionLog extends BaseEntity {

    /**
     * 状态。
     * <p>
     * 通常是 http status code 值。
     * <p>
     * 参考 {@link HttpStatus.Series} 可分为：
     * <p>
     * 1. 1xx INFORMATIONAL
     * <p>
     * 2. 2xx SUCCESSFUL
     * <p>
     * 3. 3xx REDIRECTION
     * <p>
     * 2. 4xx CLIENT_ERROR
     * <p>
     * 5. 5xx SERVER_ERROR
     *
     * @see HttpStatus#value()
     */
    private int status;
    /**
     * 错误。
     * <p>
     * 通常是 http status reason phrase 描述。
     *
     * @see HttpStatus#getReasonPhrase()
     */
    private String error;
    /**
     * 请求方法。
     * <p>
     * 一般是 GET POST PUT DELETE 这几种。
     *
     * @see org.springframework.http.HttpMethod
     * @see HttpServletRequest#getMethod()
     */
    private String method;
    /**
     * 请求 URL。
     * <p>
     * 比如：
     * <pre>
     *     <a href="http://localhost:8080/path/to">http://localhost:8080/path/to</a>
     * </pre>
     * 注意，不包含请求头、请求参数以及请求体。
     *
     * @see HttpServletRequest#getRequestURL()
     */
    private String url;
    /**
     * 请求的查询参数。
     *
     * @see HttpServletRequest#getQueryString()
     */
    private String query;
    /**
     * 请求的地址。
     *
     * @see HttpServletRequest#getRemoteAddr()
     */
    private String address;
    /**
     * 请求的会话 ID。
     *
     * @see HttpServletRequest#getRequestedSessionId()
     */
    private String sessionId;
    /**
     * 消息。
     * <p>
     * 具体异常的消息内容。
     *
     * @see Exception#getMessage()
     */
    private String message;
    /**
     * 代码。
     * <p>
     * 根据相关信息得到的异常代码，用于生产环境的展示。
     * <p>
     * 这里保存一份异常代码，用于未来对异常情况进行分析时，提取经典的异常情况，用于公示。
     */
    private String code;
    /**
     * 痕迹。
     * <p>
     * 用于后台分析错误。
     *
     * @see com.google.common.base.Throwables#getStackTraceAsString(Throwable)
     */
    @Column(length = 2000)
    private String trace;
    /**
     * 时间戳。
     * <p>
     * 异常发生时的 UTC 时间戳。
     * <p>
     * 此时间戳在转为数据时，应变成本地时间并进行友好的格式化处理。
     *
     * @see Instant
     * @see java.time.LocalDateTime
     */
    @CreatedDate
    private Instant timestamp;
    /**
     * 操作人。
     * <p>
     * 异常发生是由谁操作导致，通常是用户名称。
     *
     * @see Authentication#getPrincipal()
     * @see UserDetails#getUsername()
     */
    @CreatedBy
    private String operator;

    protected Instant getTimestamp() {
        return timestamp;
    }

    /**
     * 本地时间戳。
     * <p>
     * 将 UTC 时间戳转为本地时区的时间戳。
     */
    public LocalDateTime localTimestamp() {
        return LocalDateTime.ofInstant(timestamp, ZoneId.systemDefault());
    }
}
