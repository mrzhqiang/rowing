package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableExcerpt;
import org.springframework.data.rest.core.config.Projection;

/**
 * 字典项表单。
 * <p>
 */
@Projection(name = "dict-item-form", types = {DictItem.class})
public interface DictItemForm extends AuditableExcerpt {

    String getLabel();

    String getValue();

}
