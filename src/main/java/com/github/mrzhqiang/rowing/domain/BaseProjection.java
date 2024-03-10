package com.github.mrzhqiang.rowing.domain;

import org.springframework.data.rest.core.config.Projection;

/**
 * 基础投影。
 * <p>
 * 通常用于编辑表单的投影，返回主键字符串给前端确定修改的数据。
 *
 * @see Projection 可以将实体投影为另外一种数据形式，保护隐私，方便将实体转换为需要的数据格式。
 */
@Projection(name = "base-projection", types = {BaseEntity.class})
public interface BaseProjection {

    /**
     * 获取 ID 主键。
     *
     * @return 主键的字符串类型。
     */
    String getId();

}
