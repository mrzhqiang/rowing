package com.github.mrzhqiang.rowing.api.system.init;

import com.github.mrzhqiang.rowing.api.domain.BaseEntity;
import com.github.mrzhqiang.rowing.api.domain.TaskStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class SysInit extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(unique = true, nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    private Integer ordered;

    public boolean hasFinished() {
        return TaskStatus.FINISHED.equals(status);
    }

    /**
     * 系统初始化类型。
     * <p>
     * 注意：用于实体字段的枚举类型，一定不能调整枚举值的先后顺序，否则将导致数据错乱甚至程序异常。
     */
    public enum Type {
        /**
         * 可选的初始化类型。
         * <p>
         * 表示此类初始化操作，由用户决定是否选择执行，
         */
        OPTIONAL,
        /**
         * 必需的初始化类型。
         * <p>
         * 表示此类初始化操作，在系统启动时自动执行。
         */
        REQUIRED,
    }
}
