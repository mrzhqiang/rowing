package com.github.mrzhqiang.rowing.core.action;

import com.github.mrzhqiang.rowing.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class ActionLog extends BaseEntity {

    /**
     * 操作名称
     */
    private String action;
    /**
     * 操作类型
     */
    @Enumerated(EnumType.STRING)
    private ActionType type;
    /**
     * 操作所在类
     */
    private String target;
    /**
     * 操作所在方法
     */
    private String method;
    /**
     * 操作所在方法参数
     */
    private String params;
    /**
     * 操作状态
     */
    @Enumerated(EnumType.STRING)
    private ActionState state;
    /**
     * 操作返回结果
     * <p>
     * Json 序列化字符串
     */
    private String result;
    /**
     * 操作人的用户名称
     */
    private String operator;
    /**
     * 操作人的客户端 IP 地址
     */
    private String ip;
    /**
     * 操作人所在位置（由 IP 异步从网络获得（如果不可用则从本地数据库获得）
     */
    private String location;
    /**
     * 操作人所使用的设备信息。
     * <p>
     * 由 Header 头的 User-Agent 信息得到。
     */
    private String device;
}
