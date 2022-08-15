package com.github.mrzhqiang.rowing.system.init;

import com.github.mrzhqiang.rowing.domain.BaseEntity;
import com.github.mrzhqiang.rowing.domain.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.Instant;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class SysInit extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;
    @Column(unique = true, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;
    private Instant startTime;
    private Instant endTime;
    private String errorMessage;
    @Column(length = 2000)
    private String errorTrace;
    @Column(nullable = false)
    private Integer ordered;

    /**
     * 系统初始化类型。
     */
    @Getter
    public enum Type {
        /**
         * 可选类型。
         * <p>
         * 表示此类初始化操作，由用户决定是否选择执行，
         */
        OPTIONAL("可选"),
        /**
         * 必选类型。
         * <p>
         * 表示此类初始化操作，在系统启动时自动执行。
         */
        REQUIRED("必选"),
        ;

        /**
         * 标签。
         * <p>
         * 用于展示在前端页面的类型标签，未来如果需要国际化，则这里需要设为国际化 message key 值。
         */
        final String label;

        Type(String label) {
            this.label = label;
        }
    }
}
