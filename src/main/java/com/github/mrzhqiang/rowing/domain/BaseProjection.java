package com.github.mrzhqiang.rowing.domain;

import org.springframework.data.rest.core.config.Projection;

/**
 * 基础投影。
 * <p>
 * 通常作为表单投影的基础接口。
 *
 * @see Projection 可以将实体投影为另外一种数据形式，保护隐私，方便转换。
 */
@Projection(name = "base-projection", types = {BaseEntity.class})
public interface BaseProjection {

    String getId();

}
