package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 字典项摘要。
 * <p>
 */
@Projection(name = "dict-item-excerpt", types = {DictItem.class})
public interface DictItemExcerpt extends AuditableProjection {

    String getLabel();

    String getValue();

    @Value("#{target.group?.code}")
    String getCode();

}
