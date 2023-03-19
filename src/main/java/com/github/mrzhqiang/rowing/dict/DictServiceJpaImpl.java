package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.domain.ClassScanner;
import com.github.mrzhqiang.rowing.domain.DictType;
import com.github.mrzhqiang.rowing.util.Cells;
import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.rest.webmvc.json.EnumTranslator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 字典服务的 JPA 实现。
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DictServiceJpaImpl implements DictService {

    private final DictGroupRepository groupRepository;
    private final DictItemRepository itemRepository;
    private final ClassScanner scanner;
    private final MessageSourceAccessor sourceAccessor;
    private final EnumTranslator enumTranslator;

    public DictServiceJpaImpl(DictGroupRepository groupRepository,
                              DictItemRepository itemRepository,
                              ClassScanner scanner,
                              MessageSource messageSource,
                              EnumTranslator enumTranslator) {
        this.groupRepository = groupRepository;
        this.itemRepository = itemRepository;
        this.scanner = scanner;
        this.sourceAccessor = new MessageSourceAccessor(messageSource);
        this.enumTranslator = enumTranslator;
    }

    @Override
    public void syncInternal(String basePackage) {
        scanner.scanEnumBy(basePackage).forEach(this::handleEnum);
    }

    private void handleEnum(Class<? extends Enum<?>> enumClass) {
        Preconditions.checkNotNull(enumClass, "enum class == null");

        String simpleName = enumClass.getSimpleName();
        // 枚举类名称是大写开头的驼峰命名，转为带 '_' 连接符的大写名称，作为内置数据字典的 code 关键字
        String code = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, simpleName);
        // 通过从 message.properties 文件中获取枚举类的国际化内容，如果它不存在则使用枚举类的简称作为字典组名称
        String name = sourceAccessor.getMessage(enumClass.getName(), simpleName);

        // 通过 code 找到数据字典组，如果存在就更新，如果不存在就创建
        DictGroup dictGroup = groupRepository.findByCode(code).orElseGet(DictGroup::new);
        dictGroup.setName(name);
        dictGroup.setCode(code);
        dictGroup.setType(DictType.INTERNAL);
        groupRepository.save(dictGroup);

        String groupMessage = sourceAccessor.getMessage("DictService.syncInternal.group",
                new Object[]{name, code},
                Strings.lenientFormat("内置字典组 %s-%s 已同步", name, code));
        log.info(groupMessage);

        // 以字典项的 value 作为 Map key 避免重复
        Map<String, DictItem> itemMap = Optional.ofNullable(dictGroup.getItems())
                .map(it -> it.stream().collect(Collectors.toMap(DictItem::getValue, Function.identity())))
                .orElse(Maps.newHashMap());
        // 枚举常量不可能有重复值，所以不需要再 put 到 itemMap 中
        for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
            // 通过枚举翻译器（即 rest-message.properties 国际化文件）枚举值对应的标签
            // 比如 com.xxx.EnumClassName.ENUM_VALUE
            String label = enumTranslator.asText(enumConstant);
            String itemValue = enumConstant.name();
            // 存在即更新，否则就创建
            DictItem dictItem = Optional.ofNullable(itemMap.get(itemValue)).orElseGet(DictItem::new);
            dictItem.setGroup(dictGroup);
            dictItem.setLabel(label);
            dictItem.setValue(itemValue);
            itemRepository.save(dictItem);

            String itemMessage = sourceAccessor.getMessage("DictService.syncInternal.item",
                    new Object[]{label, itemValue, name},
                    Strings.lenientFormat("内置字典项 %s-%s 已同步到 %s 字典组", label, itemValue, name));
            log.info(itemMessage);
        }
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
                String notFoundMessage = sourceAccessor.getMessage("DictService.importExcel.notFound",
                        new Object[]{GROUP_SHEET_NAME},
                        Strings.lenientFormat("未找到名为 %s 的 Sheet 页", GROUP_SHEET_NAME));
                log.warn(notFoundMessage);
                return;
            }

            // 尝试处理字典组，生成相关实体，并返回以名称为 key 的映射
            Map<String, DictGroup> groupMap = attemptHandleGroup(group);
            if (groupMap.isEmpty()) {
                String invalidMessage = sourceAccessor.getMessage("DictService.importExcel.invalid",
                        new Object[]{},
                        Strings.lenientFormat("Excel 文件 %s 不存在有效字典组数据", excelFile));
                throw new RuntimeException(invalidMessage);
            }

            Sheet item = workbook.getSheet(ITEM_SHEET_NAME);
            if (item == null) {
                String notFoundSheetMessage = sourceAccessor.getMessage("DictService.importExcel.notFound",
                        new Object[]{ITEM_SHEET_NAME},
                        Strings.lenientFormat("未找到名为 %s 的 Sheet 页", ITEM_SHEET_NAME));
                log.warn(notFoundSheetMessage);
                return;
            }

            attemptHandleItem(groupMap, item);
        } catch (IOException e) {
            String exceptionMessage = sourceAccessor.getMessage("DictService.importExcel.exception",
                    new Object[]{excelFile},
                    Strings.lenientFormat("读取 Excel 文件 %s 出错", excelFile));
            throw new RuntimeException(exceptionMessage, e);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private Map<String, DictGroup> attemptHandleGroup(Sheet group) {
        // getPhysicalNumberOfRows 不一定是真实数据行数，但适合作为初始大小，尽量避免 put 时触发的扩容操作
        Map<String, DictGroup> groupMap = Maps.newHashMapWithExpectedSize(group.getPhysicalNumberOfRows());

        boolean skipHeader = true;
        for (Row cells : group) {
            if (skipHeader) {
                // 我们只跳过第一行，因为它是标题
                skipHeader = false;
                continue;
            }

            String name = Cells.ofString(cells.getCell(0));
            // 如果发现 null 值或空串，视为结束行
            if (Strings.isNullOrEmpty(name)) {
                String emptyNameMessage = sourceAccessor.getMessage("DictService.importExcel.empty.name",
                        new Object[]{cells.getRowNum()},
                        Strings.lenientFormat(
                                "发现第 %s 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                log.info(emptyNameMessage);
                break;
            }

            String code = Cells.ofString(cells.getCell(1));
            if (Strings.isNullOrEmpty(code)) {
                String emptyCodeMessage = sourceAccessor.getMessage("DictService.importExcel.empty.code",
                        new Object[]{cells.getRowNum()},
                        Strings.lenientFormat(
                                "发现第 %s 行 code 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                log.info(emptyCodeMessage);
                break;
            }

            DictGroup entity = groupRepository.findByCode(code).orElseGet(DictGroup::new);
            entity.setName(name);
            entity.setCode(code);
            entity.setType(DictType.EXCEL);
            // save 方法本身带有事务，然后当前 service public 方法也带有事务
            // 根据 REQUIRED 传播类型，则 save 会自动加入 service 的当前事务
            groupMap.put(code, groupRepository.save(entity));

            String groupMessage = sourceAccessor.getMessage("DictService.importExcel.group",
                    new Object[]{name, code},
                    Strings.lenientFormat("Excel 字典组 %s-%s 已同步", name, code));
            log.info(groupMessage);
        }
        return groupMap;
    }

    @SuppressWarnings("DuplicatedCode")
    private void attemptHandleItem(Map<String, DictGroup> groupMap, Sheet item) {
        boolean skipHeader = true;
        for (Row cells : item) {
            if (skipHeader) {
                skipHeader = false;
                continue;
            }

            String code = Cells.ofString(cells.getCell(0));
            if (Strings.isNullOrEmpty(code)) {
                String emptyCodeMessage = sourceAccessor.getMessage("DictService.importExcel.empty.code",
                        new Object[]{cells.getRowNum()},
                        Strings.lenientFormat(
                                "发现第 %s 行 code 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                log.info(emptyCodeMessage);
                break;
            }

            DictGroup dictGroup = groupMap.get(code);
            if (dictGroup == null) {
                String codeNotFoundMessage = sourceAccessor.getMessage("DictService.importExcel.code.notFound",
                        new Object[]{code},
                        Strings.lenientFormat("错误的字典项，指定的 code %s 在 group Sheet 页中不存在", code));
                log.warn(codeNotFoundMessage);
                continue;
            }

            String label = Cells.ofString(cells.getCell(1));
            if (Strings.isNullOrEmpty(label)) {
                String emptyLabelMessage = sourceAccessor.getMessage("DictService.importExcel.empty.label",
                        new Object[]{cells.getRowNum()},
                        Strings.lenientFormat(
                                "发现第 %s 行 label 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                log.info(emptyLabelMessage);
                break;
            }

            String value = Cells.ofString(cells.getCell(2));
            if (Strings.isNullOrEmpty(value)) {
                String emptyValueMessage = sourceAccessor.getMessage("DictService.importExcel.empty.value",
                        new Object[]{cells.getRowNum()},
                        Strings.lenientFormat(
                                "发现第 %s 行 value 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                log.info(emptyValueMessage);
                break;
            }

            DictItem entity = itemRepository.findByGroup_CodeAndValue(code, value).orElseGet(DictItem::new);
            entity.setLabel(label);
            entity.setValue(value);
            entity.setGroup(dictGroup);
            itemRepository.save(entity);

            String itemMessage = sourceAccessor.getMessage("DictService.importExcel.item",
                    new Object[]{label, value, code},
                    Strings.lenientFormat("Excel 字典项 %s-%s 已同步到 %s 字典组", label, value, code));
            log.info(itemMessage);
        }
    }
}
