package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.BaseProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 字典项表单。
 */
@Projection(name = "dict-item-form", types = {DictItem.class})
public interface DictItemForm extends BaseProjection {

    String getLabel();

    String getValue();

}
