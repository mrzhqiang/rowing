package com.github.mrzhqiang.rowing.system.data;

import com.github.mrzhqiang.rowing.util.Cells;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class DataDictServiceCachedJpaImpl implements DataDictService {

    private static final String GROUP_SHEET_NAME = "group";
    private static final String ITEM_SHEET_NAME = "item";
    private static final DateTimeFormatter SERIAL_NO_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final DataDictGroupRepository groupRepository;

    private final DataDictItemRepository itemRepository;

    public DataDictServiceCachedJpaImpl(DataDictGroupRepository groupRepository,
                                        DataDictItemRepository itemRepository) {
        this.groupRepository = groupRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public void importExcel(File excelFile) {
        Preconditions.checkNotNull(excelFile, "excel file == null");
        Preconditions.checkArgument(excelFile.exists(), "excel file must be exists");
        Preconditions.checkArgument(!excelFile.isDirectory(), "excel file must be not directory");

        // 以只读模式打开数据字典 Excel 文件
        try (Workbook workbook = WorkbookFactory.create(excelFile)) {
            Sheet group = workbook.getSheet(GROUP_SHEET_NAME);
            if (group == null) {
                log.warn("未找到名为 group 的 Sheet 页");
                return;
            }

            // 文件有效，那么我们清理所有旧数据
            groupRepository.deleteAll();

            // 根据 group 页数据解析组映射
            Map<String, DataDictGroup> groupMap = attemptHandleGroup(group, excelFile.getAbsolutePath());
            if (groupMap.isEmpty()) {
                String message = Strings.lenientFormat("Excel %s 文件无效，将撤销当前改动", excelFile);
                throw new RuntimeException(message);
            }

            Sheet item = workbook.getSheet(ITEM_SHEET_NAME);
            if (item == null) {
                log.warn("未找到名为 item 的 Sheet 页");
                return;
            }

            attemptHandleItem(groupMap, item);
        } catch (IOException e) {
            String message = Strings.lenientFormat("读取 Excel 文件 %s 出错", excelFile);
            throw new RuntimeException(message, e);
        }
    }

    private Map<String, DataDictGroup> attemptHandleGroup(Sheet group, String filename) {
        boolean skipHeader = true;
        String serialNo = LocalDateTime.now().format(SERIAL_NO_FORMATTER);
        Map<String, DataDictGroup> groupMap = Maps.newHashMapWithExpectedSize(group.getPhysicalNumberOfRows());

        for (Row cells : group) {
            if (skipHeader) {
                // 我们只跳过第一行，因为它是标题
                skipHeader = false;
                continue;
            }

            String name = Cells.ofString(cells.getCell(0));
            if (Strings.isNullOrEmpty(name) || CharMatcher.whitespace().matchesAllOf(name)) {
                log.warn("发现第 {} 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            String code = Cells.ofString(cells.getCell(1));
            if (Strings.isNullOrEmpty(code) || CharMatcher.whitespace().matchesAnyOf(code)) {
                log.warn("发现第 {} 行 code 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            DataDictGroup entity = new DataDictGroup();
            entity.setName(name);
            entity.setCode(code);
            entity.setSource(filename);
            entity.setSerialNo(serialNo);
            entity.setType(DataDictGroup.Type.DEFAULT);
            groupMap.put(code, groupRepository.save(entity));
        }
        return groupMap;
    }

    private void attemptHandleItem(Map<String, DataDictGroup> groupMap, Sheet item) {
        boolean skipHeader = true;

        for (Row cells : item) {
            if (skipHeader) {
                // 我们只跳过第一行，因为它是标题
                skipHeader = false;
                continue;
            }

            String parent = Cells.ofString(cells.getCell(0));
            if (Strings.isNullOrEmpty(parent) || CharMatcher.whitespace().matchesAnyOf(parent)) {
                log.warn("发现第 {} 行 parent 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }
            DataDictGroup dictGroup = groupMap.get(parent);
            if (dictGroup == null) {
                log.warn("错误的字典项，指定的 parent {} 在 group 中不存在", parent);
                continue;
            }

            String label = Cells.ofString(cells.getCell(1));
            if (Strings.isNullOrEmpty(label) || CharMatcher.whitespace().matchesAnyOf(label)) {
                log.warn("发现第 {} 行 label 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            String value = Cells.ofString(cells.getCell(2));
            if (Strings.isNullOrEmpty(value) || CharMatcher.whitespace().matchesAnyOf(value)) {
                log.warn("发现第 {} 行 value 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            DataDictItem entity = new DataDictItem();
            entity.setLabel(label);
            entity.setValue(value);
            entity.setGroup(dictGroup);
            entity.setSerialNo(dictGroup.getSerialNo());
            // save 方法本身带有事务，然后当前 service public 方法也带有事务
            // 根据默认传播规则，save 会自动加入 service 的当前事务
            // 因此如果保存出现异常，会自动回滚事务
            itemRepository.save(entity);
        }
    }
}
