package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 行政区划代码表单。
 */
@Projection(name = "dict-gbt-2260-form", types = {DictGBT2260.class})
public interface DictGBT2260Form extends BaseProjection {

    @Value("#{target.parent?.id}")
    String getParentId();

    String getName();

    String getCode();

    String getLevel();

}
