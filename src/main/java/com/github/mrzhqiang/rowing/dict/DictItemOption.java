package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 字典项选项。
 */
@Projection(name = "dict-item-option", types = {DictItem.class})
public interface DictItemOption extends BaseProjection {

    String getLabel();

    String getValue();

}
