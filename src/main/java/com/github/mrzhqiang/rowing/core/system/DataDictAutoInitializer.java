package com.github.mrzhqiang.rowing.core.system;

import com.github.mrzhqiang.rowing.core.system.BaseAutoInitializer;
import com.github.mrzhqiang.rowing.core.system.data.DataDictGroup;
import com.github.mrzhqiang.rowing.core.system.data.DataDictGroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DataDictAutoInitializer extends BaseAutoInitializer {

    private final DataDictGroupService groupService;

    @Value(ResourceUtils.CLASSPATH_URL_PREFIX + "data/data-dict.xlsx")
    private Resource resource;

    public DataDictAutoInitializer(DataDictGroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public void attemptInitialize() throws Exception {
        Map<String, DataDictGroup> groupMap = groupService.importExcel(resource.getFile());

        /*for (DataDictGroupForm group : groups) {
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
        }*/
    }
}
