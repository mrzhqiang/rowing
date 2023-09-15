package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import com.github.mrzhqiang.rowing.domain.SettingType;
import org.springframework.data.rest.core.config.Projection;

/**
 * 设置摘要。
 */
@Projection(name = "setting-excerpt", types = {Setting.class})
public interface SettingExcerpt extends AuditableProjection {

    SettingType getType();

    String getName();

    String getCode();

    String getContent();

    String getStyle();

}
