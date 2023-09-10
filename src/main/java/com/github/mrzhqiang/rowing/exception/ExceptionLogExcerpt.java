package com.github.mrzhqiang.rowing.exception;

import com.github.mrzhqiang.rowing.domain.BaseExcerpt;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

/**
 * 异常日志摘要。
 */
@Projection(name = "exception-log-excerpt", types = {ExceptionLog.class})
public interface ExceptionLogExcerpt extends BaseExcerpt {

    Integer getStatus();

    String getError();

    String getMethod();

    String getUrl();

    String getQuery();

    String getAddress();

    String getSessionId();

    String getMessage();

    String getCode();

    String getTrace();

    LocalDateTime getTimestamp();

    String getOperator();

}
