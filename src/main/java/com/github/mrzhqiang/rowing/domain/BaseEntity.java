package com.github.mrzhqiang.rowing.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.ProxyUtils;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Objects;

/**
 * 基础实体。
 * <p>
 * 最基本的实体，定义了 Long 类型主键和 Long 类型版本号。
 * <p>
 * 关于主键：
 * <p>
 * 很多系统都不再使用自增长的 Long 类型作为主键，原因是他们认为不利于数据库迁移和高并发使用。
 * <p>
 * 本系统之所以继续使用，是因为基于 JPA 框架的数据库架构无需手动迁移，它可以无缝对接主流数据库。
 * <p>
 * 另外：如果担心通过自增长 id 推断业务数据，那么只要做好 ACL 数据权限控制，即避免横向越权，那么也不是什么大问题。
 * <p>
 * 关于版本号：
 * <p>
 * 这是一个乐观锁设计，期望数据在读取后的一段时间内不会被修改，以便于更新数据时，无需开辟临界区。
 * <p>
 * 同时还可以被 <a href="https://docs.spring.io/spring-data/rest/docs/current/reference/html/#conditional">Spring Data Rest</a>
 * 框架用于 API 接口的响应 Header 标记，其中主要是 ETag 标记，它被用于更新时的乐观锁检测，以便于预测本次更新请求是否执行成功。
 */
@SuppressWarnings("serial")
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    /**
     * ID 编号。
     * <p>
     * 作为主键，拥有非空约束，使用自增长生成策略。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 版本号。
     * <p>
     * 由 JPA 框架自动赋值，作为乐观锁防止脏数据产生。
     */
    @JsonIgnore
    @Version
    private Long version;

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), this.id);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        if (null == o) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!getClass().equals(ProxyUtils.getUserClass(o))) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

}
