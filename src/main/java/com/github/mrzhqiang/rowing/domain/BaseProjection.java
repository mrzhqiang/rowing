package com.github.mrzhqiang.rowing.domain;

import org.springframework.data.rest.core.config.Projection;

/**
 * 基础投影。
 * <p>
 * 转换实体时，提供统一的风格。
 * <p>
 * 摘要：作为数据列表的预览，类似缩略图一样，仅返回关键信息，可以节省资源。
 */
@Projection(name = "base-projection", types = {BaseEntity.class})
public interface BaseProjection {

    String getId();

}
