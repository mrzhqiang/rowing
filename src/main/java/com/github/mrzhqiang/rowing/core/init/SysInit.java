package com.github.mrzhqiang.rowing.core.init;

import com.github.mrzhqiang.rowing.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
public class SysInit extends BaseEntity {

    @Enumerated
    private Type type;
    @Column(unique = true, nullable = false)
    private String name;
    @Enumerated
    private Status status;
    private Integer ordered;

    public boolean hasFinished() {
        return Status.FINISHED.equals(status);
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

    /**
     * 系统初始化状态。
     * <p>
     * 注意：用于实体字段的枚举类型，一定不能调整枚举值的先后顺序，否则将导致数据错乱甚至程序异常。
     */
    public enum Status {
        /**
         * 初始化默认状态。
         * <p>
         * 表示任务未完成，这有可能是未执行过、正在执行中以及执行出错，我们统一视为默认状态。
         */
        DEFAULT,
        /**
         * 初始化已完成状态。
         * <p>
         * 表示任务已完成，过程是艰难的，但结果是极好的。
         */
        FINISHED,
    }
}
