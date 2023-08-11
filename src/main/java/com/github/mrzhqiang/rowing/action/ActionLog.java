package com.github.mrzhqiang.rowing.action;

import com.github.mrzhqiang.rowing.domain.ActionState;
import com.github.mrzhqiang.rowing.domain.ActionType;
import com.github.mrzhqiang.rowing.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 操作日志。
 * <p>
 * 记录标记 {@link Action} 注解的方法操作日志。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ActionLog extends BaseEntity {

    private static final long serialVersionUID = 7299201576558653855L;

    /**
     * 类名称字符串的最大长度。
     */
    public static final int MAX_CLASS_NAME_LENGTH = 200;
    /**
     * 方法名称字符串的最大长度。
     */
    public static final int MAX_METHOD_NAME_LENGTH = 200;
    /**
     * 方法参数字符串的最大长度。
     */
    public static final int MAX_PARAMS_LENGTH = 2000;

    /**
     * 操作名称。
     *
     * @see Action#value()
     * @see ActionType
     */
    @Column(length = MAX_ENUM_NAME_LENGTH)
    private String action;
    /**
     * 操作类型。
     */
    @Enumerated(EnumType.STRING)
    @Column(length = MAX_ENUM_NAME_LENGTH)
    private ActionType type = ActionType.NONE;
    /**
     * 操作所在类。
     */
    @Column(length = MAX_CLASS_NAME_LENGTH)
    private String target;
    /**
     * 操作所在方法。
     */
    @Column(length = MAX_METHOD_NAME_LENGTH)
    private String method;
    /**
     * 操作所在方法参数。
     */
    @Column(length = MAX_PARAMS_LENGTH)
    private String params;
    /**
     * 操作状态。
     */
    @Enumerated(EnumType.STRING)
    @Column(length = MAX_ENUM_NAME_LENGTH)
    private ActionState state;
    /**
     * 操作返回结果.
     * <p>
     * Json 序列化字符串.
     */
    @Column(columnDefinition = "text")
    private String result;
    /**
     * 操作人。
     * <p>
     * 操作人的用户名称.
     */
    @Column(length = MAX_USERNAME_LENGTH)
    private String operator;
    /**
     * IP 地址。
     * <p>
     * 操作人的客户端 IP 地址
     */
    @Column(length = 128)
    private String ip;
    /**
     * 地理位置。
     * <p>
     * 操作人所在位置（由 IP 异步从网络获得（如果不可用则从本地数据库获得）
     */
    @Column(length = 100)
    private String location;
    /**
     * 操作人所使用的设备信息。
     * <p>
     * 由 Header 头的 User-Agent 信息得到。
     * <p>
     * Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/115.0.0.0 Safari/537.36 Edg/115.0.1901.200
     */
    @Column(length = 200)
    private String device;
}
