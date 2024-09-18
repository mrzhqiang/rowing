package com.github.mrzhqiang.rowing.action;

import com.github.mrzhqiang.rowing.domain.ActionState;
import com.github.mrzhqiang.rowing.domain.ActionType;
import com.github.mrzhqiang.rowing.domain.BaseEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.Instant;

/**
 * 操作日志。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ActionLog extends BaseEntity {

    private static final long serialVersionUID = 7299201576558653855L;

    /**
     * 操作类型。
     */
    @Enumerated(EnumType.STRING)
    @Column(length = Domains.ENUM_LENGTH)
    private ActionType type = ActionType.NONE;
    /**
     * 操作所在类。
     */
    @Column(length = Domains.CLASS_NAME_LENGTH)
    private String target;
    /**
     * 操作所在方法。
     */
    @Column(length = Domains.METHOD_NAME_LENGTH)
    private String method;
    /**
     * 操作所在方法参数。
     */
    @Column(length = Domains.METHOD_PARAMS_LENGTH)
    private String params;
    /**
     * 操作状态。
     */
    @Enumerated(EnumType.STRING)
    @Column(length = Domains.ENUM_LENGTH)
    private ActionState state;
    /**
     * 操作返回结果.
     * <p>
     * Json 序列化字符串.
     */
    @Column(columnDefinition = Domains.TEXT_COLUMN_TYPE)
    private String result;
    /**
     * 操作人。
     * <p>
     * 操作人的用户名称.
     */
    @CreatedBy
    @Column(length = Domains.USERNAME_MAX_LENGTH)
    private String operator;
    /**
     * IP 地址。
     * <p>
     * 操作人的客户端 IP 地址
     */
    @Column(length = Domains.IP_ADDRESS_LENGTH)
    private String ip;
    /**
     * 地理位置。
     * <p>
     * 操作人所在位置（由 IP 异步从网络获得（如果不可用则从本地数据库获得）
     */
    @Column(length = Domains.LOCATION_LENGTH)
    private String location;
    /**
     * 操作人所使用的设备信息。
     * <p>
     * 由 Header 头的 User-Agent 信息得到。
     * <p>
     * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36 Edg/115.0.1901.200
     */
    @Column(length = Domains.DEVICE_INFO_LENGTH)
    private String device;

    /**
     * 操作时间戳。
     */
    @CreatedDate
    private Instant timestamp;

}
