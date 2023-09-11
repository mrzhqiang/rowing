package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.AuditableProjection;
import org.springframework.data.rest.core.config.Projection;

/**
 * 设置表单。
 */
@Projection(name = "setting-form", types = {Setting.class})
public interface SettingForm extends AuditableProjection {

    String getType();

    String getName();

    String getCode();

    String getContent();

}
