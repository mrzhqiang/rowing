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
import java.util.Map;

@Slf4j
@Service
@Transactional
public class DataDictServiceCachedJpaImpl implements DataDictService {

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

        // WorkbookFactory 支持创建 HSSFWorkbook 和 XSSFWorkbook 实例
        try (Workbook workbook = WorkbookFactory.create(excelFile)) {
            Sheet group = workbook.getSheet(GROUP_SHEET_NAME);
            if (group == null) {
                log.warn("未找到名为 {} 的 Sheet 页", GROUP_SHEET_NAME);
                return;
            }

            // 存在字典组 Sheet 页，先删除所有内置字典数据——当删除字典组时，将级联删除字典项
            groupRepository.deleteAllByType(DataDictGroup.Type.DEFAULT);

            // 尝试处理字典组，生成相关实体，并返回以名称为 key 的映射
            Map<String, DataDictGroup> groupMap = attemptHandleGroup(group);
            if (groupMap.isEmpty()) {
                String message = Strings.lenientFormat("Excel 文件 %s 不存在有效字典组数据", excelFile);
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
    private Map<String, DataDictGroup> attemptHandleGroup(Sheet group) {
        // getPhysicalNumberOfRows 不一定是真实数据行数，但适合作为初始大小，尽量避免 put 时触发的扩容操作
        Map<String, DataDictGroup> groupMap = Maps.newHashMapWithExpectedSize(group.getPhysicalNumberOfRows());

        boolean skipHeader = true;
        for (Row cells : group) {
            if (skipHeader) {
                skipHeader = false;
                continue;
            }

            String name = Cells.ofString(cells.getCell(0));
            if (Strings.isNullOrEmpty(name) || CharMatcher.whitespace().matchesAllOf(name)) {
                log.warn("发现第 {} 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            String code = Cells.ofString(cells.getCell(1));
            if (Strings.isNullOrEmpty(code) || CharMatcher.whitespace().matchesAllOf(code)) {
                log.warn("发现第 {} 行 code 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            DataDictGroup entity = new DataDictGroup();
            entity.setName(name);
            entity.setCode(code);
            entity.setType(DataDictGroup.Type.DEFAULT);
            // save 方法本身带有事务，然后当前 service public 方法也带有事务
            // 根据传播类型 REQUIRED，则 save 会自动加入 service 的当前事务
            groupMap.put(code, groupRepository.save(entity));
        }
        return groupMap;
    }

    @SuppressWarnings("DuplicatedCode")
    private void attemptHandleItem(Map<String, DataDictGroup> groupMap, Sheet item) {
        boolean skipHeader = true;
        for (Row cells : item) {
            if (skipHeader) {
                // 我们只跳过第一行，因为它是标题
                skipHeader = false;
                continue;
            }

            String parent = Cells.ofString(cells.getCell(0));
            if (Strings.isNullOrEmpty(parent) || CharMatcher.whitespace().matchesAllOf(parent)) {
                log.warn("发现第 {} 行 parent 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            DataDictGroup dictGroup = groupMap.get(parent);
            if (dictGroup == null) {
                log.warn("错误的字典项，指定的 parent {} 在 group 中不存在", parent);
                continue;
            }

            String label = Cells.ofString(cells.getCell(1));
            if (Strings.isNullOrEmpty(label) || CharMatcher.whitespace().matchesAllOf(label)) {
                log.warn("发现第 {} 行 label 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            String value = Cells.ofString(cells.getCell(2));
            if (Strings.isNullOrEmpty(value) || CharMatcher.whitespace().matchesAllOf(value)) {
                log.warn("发现第 {} 行 value 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                break;
            }

            DataDictItem entity = new DataDictItem();
            entity.setLabel(label);
            entity.setValue(value);
            entity.setGroup(dictGroup);
            itemRepository.save(entity);
        }
    }
}
