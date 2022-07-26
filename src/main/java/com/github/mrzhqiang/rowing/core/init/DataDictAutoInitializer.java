package com.github.mrzhqiang.rowing.core.init;

import com.google.common.base.Stopwatch;
import hellgate.common.system.DataDictGroup;
import hellgate.common.system.DataDictGroupData;
import hellgate.common.system.DataDictGroupMapper;
import hellgate.common.system.DataDictGroupRepository;
import hellgate.common.system.DataDictItem;
import hellgate.common.system.DataDictItemMapper;
import hellgate.common.system.DataDictItemRepository;
import hellgate.common.util.Jsons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DataDictAutoInitializer extends BaseAutoInitializer {

    private final DataDictGroupRepository groupRepository;
    private final DataDictGroupMapper groupMapper;
    private final DataDictItemRepository itemRepository;
    private final DataDictItemMapper itemMapper;

    @Value(ResourceUtils.CLASSPATH_URL_PREFIX + "data/data-dict.json")
    private Resource resource;

    public DataDictAutoInitializer(DataDictGroupRepository groupRepository,
                                   DataDictGroupMapper groupMapper,
                                   DataDictItemRepository itemRepository,
                                   DataDictItemMapper itemMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean onInit() throws Exception {
        log.info("开始自动初始化数据字典...");
        Stopwatch stopwatch = Stopwatch.createStarted();

        if (resource == null) {
            log.warn("数据字典 json 文件不存在，跳过初始化过程，耗时：{}", stopwatch.stop());
            return false;
        }

        File dataFile = resource.getFile();
        List<DataDictGroupData> groups = Jsons.listFromFile(dataFile, DataDictGroupData.class);
        if (CollectionUtils.isEmpty(groups)) {
            log.warn("数据字典 json 文件没有解析到数据，跳过初始化过程，耗时：{}", stopwatch.stop());
            return false;
        }

        for (DataDictGroupData group : groups) {
            log.debug("保存数据字典数据 {}", group);

            DataDictGroup groupEntity = groupMapper.toEntity(group);
            groupEntity.setType(DataDictGroup.Type.DB);
            log.debug("保存数据字典分组 {}", groupEntity);
            DataDictGroup dataDictGroup = groupRepository.save(groupEntity);

            if (CollectionUtils.isEmpty(group.getItems())) {
                log.warn("数据字典分组 {} 的字典项为空", groupEntity.getName());
                continue;
            }

            List<DataDictItem> itemList = group.getItems().stream()
                    .map(itemMapper::toEntity)
                    .peek(it -> it.setGroup(dataDictGroup))
                    .collect(Collectors.toList());
            log.debug("保存字典项 {} 到字典分组 {}", itemList, groupEntity.getName());
            itemRepository.saveAll(itemList);
        }

        log.info("数据字典自动初始化完成，耗时：{}", stopwatch.stop());
        return true;
    }
}
