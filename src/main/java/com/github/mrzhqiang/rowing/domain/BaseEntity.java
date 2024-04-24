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
 * 抽象的基础实体。
 * <p>
 * 最基础的实体，定义了 Long 类型的主键和版本号。
 * <p>
 * 关于版本号：
 * <p>
 * 这是一个乐观锁设计，期望数据在读取后的一段时间内不会被修改，以便于更新数据时，无需开辟临界区。
 * <p>
 * 同时还可以被 <a href="https://docs.spring.io/spring-data/rest/docs/3.7.14/reference/html/#conditional">Spring Data Rest</a>
 * 框架用于 API 接口响应的 Header 标记，其中主要是 ETag 标记，它被用于更新时的乐观锁检测，以便快速检测本次更新请求是否返回成功。
 */
@SuppressWarnings("serial")
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    /**
     * ID 主键。
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 版本号。
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
