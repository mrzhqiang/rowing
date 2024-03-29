package com.github.mrzhqiang.rowing.dict.gbt;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

/**
 * 行政区划代码选项。
 */
@Projection(name = "dict-gbt-2260-option", types = {DictGBT2260.class})
public interface DictGBT2260Option extends AuditableProjection {

    @Value("#{target.name}")
    String getLabel();

    @Value("#{target.level == '3'}")
    Boolean getIsDisabled();

}
