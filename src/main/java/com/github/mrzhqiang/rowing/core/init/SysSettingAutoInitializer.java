package com.github.mrzhqiang.rowing.core.init;

import com.google.common.base.Stopwatch;
import hellgate.common.system.SysSetting;
import hellgate.common.system.SysSettingData;
import hellgate.common.system.SysSettingMapper;
import hellgate.common.system.SysSettingRepository;
import hellgate.common.util.Jsons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

@Slf4j
@Service
public class SysSettingAutoInitializer extends BaseAutoInitializer {

    private final SysSettingMapper mapper;
    private final SysSettingRepository repository;

    @Value(ResourceUtils.CLASSPATH_URL_PREFIX + "data/sys-setting.json")
    private Resource resource;

    public SysSettingAutoInitializer(SysSettingMapper mapper,
                                     SysSettingRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean onInit() throws Exception {
        log.info("开始自动初始化系统设置...");
        Stopwatch stopwatch = Stopwatch.createStarted();

        if (resource == null) {
            log.warn("系统设置 json 文件不存在，跳过初始化过程，耗时：{}", stopwatch.stop());
            return false;
        }

        File dataFile = resource.getFile();
        List<SysSettingData> settings = Jsons.listFromFile(dataFile, SysSettingData.class);
        if (CollectionUtils.isEmpty(settings)) {
            log.warn("系统设置 json 文件没有解析到数据，跳过初始化过程，耗时：{}", stopwatch.stop());
            return false;
        }

        handleSettingList(settings, null);
        log.info("数据字典自动初始化完成，耗时：{}", stopwatch.stop());
        return true;
    }

    private void handleSettingList(List<SysSettingData> settings, @Nullable SysSetting parent) {
        if (CollectionUtils.isEmpty(settings)) {
            if (parent != null) {
                log.debug("系统设置 {} 的子设置为空", parent.getKey());
            }
            return;
        }

        for (SysSettingData setting : settings) {
            SysSetting entity = mapper.toEntity(setting);
            entity.setParent(parent);
            log.debug("保存系统设置数据 {}", entity);
            repository.save(entity);

            handleSettingList(setting.getChildren(), entity);
        }
    }
}
