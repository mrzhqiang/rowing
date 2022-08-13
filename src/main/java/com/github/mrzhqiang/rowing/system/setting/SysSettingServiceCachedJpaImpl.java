package com.github.mrzhqiang.rowing.system.setting;

import com.github.mrzhqiang.rowing.util.Cells;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class SysSettingServiceCachedJpaImpl implements SysSettingService {

    private final SysSettingGroupMapper groupMapper;
    private final SysSettingGroupRepository groupRepository;
    private final SysSettingItemMapper itemMapper;
    private final SysSettingItemRepository itemRepository;

    public SysSettingServiceCachedJpaImpl(SysSettingGroupMapper groupMapper,
                                          SysSettingGroupRepository groupRepository,
                                          SysSettingItemMapper itemMapper,
                                          SysSettingItemRepository itemRepository) {
        this.groupMapper = groupMapper;
        this.groupRepository = groupRepository;
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
    }

    @Override
    public void importExcel(File excelFile) {
        Preconditions.checkNotNull(excelFile, "excel file == null");
        Preconditions.checkArgument(excelFile.exists(), "excel file must be exists");
        Preconditions.checkArgument(!excelFile.isDirectory(), "excel file must be not directory");

        // WorkbookFactory 支持创建 HSSFWorkbook 和 XSSFWorkbook 实例
        try (Workbook workbook = WorkbookFactory.create(excelFile)) {
            Sheet group = workbook.getSheet(GROUP_SHEET_NAME);
            if (group == null) {
                log.warn("未找到名为 {} 的 Sheet 页", GROUP_SHEET_NAME);
                return;
            }

            groupRepository.deleteAll();

            Map<String, SysSettingGroup> groupMap = attemptHandleGroup(group);
            if (groupMap.isEmpty()) {
                String message = Strings.lenientFormat("Excel %s 文件无效，将撤销当前改动", excelFile);
                throw new RuntimeException(message);
            }

            Sheet item = workbook.getSheet(ITEM_SHEET_NAME);
            if (item == null) {
                log.warn("未找到名为 {} 的 Sheet 页", ITEM_SHEET_NAME);
                return;
            }

            attemptHandleItem(groupMap, item);
        } catch (IOException e) {
            String message = Strings.lenientFormat("读取 Excel 文件 %s 出错", excelFile);
            throw new RuntimeException(message, e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private Map<String, SysSettingGroup> attemptHandleGroup(Sheet group) {
        Map<String, SysSettingGroup> groupMap = Maps.newHashMapWithExpectedSize(group.getPhysicalNumberOfRows());

        boolean skipHeader = true;
        for (Row cells : group) {
            if (skipHeader) {
                // 我们只跳过第一行，因为它是标题
                skipHeader = false;
                continue;
            }

            String name = Cells.ofString(cells.getCell(0));
            if (Strings.isNullOrEmpty(name)) {
                log.warn("发现第 {} 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            String code = Cells.ofString(cells.getCell(1));
            if (Strings.isNullOrEmpty(code)) {
                log.warn("发现第 {} 行 code 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            if (groupRepository.existsByCode(code)) {
                log.warn("发现设置组 code {} 已存在，忽略此条数据", code);
                continue;
            }

            SysSettingGroup entity = new SysSettingGroup();
            entity.setName(name);
            entity.setCode(code);
            groupMap.put(code, groupRepository.save(entity));
        }
        return groupMap;
    }

    @SuppressWarnings("DuplicatedCode")
    private void attemptHandleItem(Map<String, SysSettingGroup> groupMap, Sheet item) {
        boolean skipHeader = true;
        for (Row cells : item) {
            if (skipHeader) {
                skipHeader = false;
                continue;
            }

            String parent = Cells.ofString(cells.getCell(0));
            if (Strings.isNullOrEmpty(parent)) {
                log.warn("发现第 {} 行 parent 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            SysSettingGroup settingGroup = groupMap.get(parent);
            if (settingGroup == null) {
                log.warn("错误的设置项，指定的 parent {} 在 group 页中不存在", parent);
                continue;
            }

            String label = Cells.ofString(cells.getCell(1));
            if (Strings.isNullOrEmpty(label)) {
                log.warn("发现第 {} 行 label 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            String name = Cells.ofString(cells.getCell(2));
            if (Strings.isNullOrEmpty(name)) {
                log.warn("发现第 {} 行 name 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            String value = Cells.ofString(cells.getCell(3));
            if (Strings.isNullOrEmpty(value)) {
                log.warn("发现第 {} 行 value 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            SysSettingItem entity = new SysSettingItem();
            entity.setLabel(label);
            entity.setName(name);
            entity.setValue(value);
            entity.setGroup(settingGroup);

            if (itemRepository.exists(Example.of(entity))) {
                log.warn("检测到实体 {} 已存在，跳过新增操作", entity);
                continue;
            }

            itemRepository.save(entity);
        }
    }
}
