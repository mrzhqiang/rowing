package com.github.mrzhqiang.rowing.modules.init;

import com.github.mrzhqiang.helper.Exceptions;
import com.github.mrzhqiang.rowing.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 初始化任务记录。
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class InitTaskLog extends BaseEntity {

    /**
     * 记录所属任务。
     */
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private InitTask task;
    /**
     * 消息。
     * <p>
     * 记录任务执行结果，如果任务失败，则记录当前异常原因。
     */
    private String message;
    /**
     * 踪迹。
     * <p>
     * 只有当任务失败时，才记录当前异常堆栈。
     */
    @Column(length = Exceptions.MAX_TRACE_LENGTH)
    private String trace;

    public static InitTaskLog of(InitTask task) {
        InitTaskLog log = new InitTaskLog();
        log.setTask(task);
        return log;
    }
}