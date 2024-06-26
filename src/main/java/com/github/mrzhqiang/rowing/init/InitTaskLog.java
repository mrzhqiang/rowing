package com.github.mrzhqiang.rowing.init;

import com.github.mrzhqiang.rowing.domain.AuditableEntity;
import com.github.mrzhqiang.rowing.domain.Domains;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 初始化任务日志。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class InitTaskLog extends AuditableEntity {

    private static final long serialVersionUID = -3132150344457512287L;

    /**
     * 任务。
     */
    @ManyToOne(optional = false)
    private InitTask task;
    /**
     * 消息。
     * <p>
     * 记录任务执行结果，如果任务失败，则记录当前异常原因。
     */
    @Column(length = Domains.EXCEPTION_MESSAGE_LENGTH)
    private String message;
    /**
     * 踪迹。
     * <p>
     * 只有当任务失败时，才记录当前异常堆栈。
     */
    @Column(length = Domains.EXCEPTION_TRACE_LENGTH)
    private String trace;

}
