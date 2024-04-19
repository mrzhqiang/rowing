package com.github.mrzhqiang.rowing.action;

import com.github.mrzhqiang.rowing.domain.ActionState;
import com.github.mrzhqiang.rowing.domain.ActionType;
import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

/**
 * 操作日志摘要。
 */
@Projection(name = "action-log-excerpt", types = {ActionLog.class})
public interface ActionLogExcerpt extends BaseProjection {

    @Value("#{target.type}")
    String getAction();

    String getType();

    String getTarget();

    String getMethod();

    String getParams();

    ActionState getState();

    String getResult();

    String getOperator();

    String getIp();

    String getLocation();

    String getDevice();

    LocalDateTime getTimestamp();

}
