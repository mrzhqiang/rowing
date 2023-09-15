package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 行政区划代码摘要。
 */
@Projection(name = "dict-gbt-2260-excerpt", types = {DictGBT2260.class})
public interface DictGBT2260Excerpt extends AuditableProjection {

    String getName();

    String getCode();

    @Value("#{target.parent?.name}")
    String getParentName();

    @Value("#{target.parent?.code}")
    String getParentCode();

    String getLevel();

}
