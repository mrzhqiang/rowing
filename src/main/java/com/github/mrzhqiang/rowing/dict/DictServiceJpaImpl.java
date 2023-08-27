package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.config.DictProperties;
import com.github.mrzhqiang.rowing.domain.DictType;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.init.InitializationException;
import com.github.mrzhqiang.rowing.util.Cells;
import com.github.mrzhqiang.rowing.util.Domains;
import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.rest.webmvc.json.EnumTranslator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DictServiceJpaImpl implements DictService {

    private final DictProperties properties;
    private final DictGroupRepository groupRepository;
    private final DictItemRepository itemRepository;
    private final ClassScanner scanner;
    private final EnumTranslator enumTranslator;

    public DictServiceJpaImpl(DictProperties properties,
                              DictGroupRepository groupRepository,
                              DictItemRepository itemRepository,
                              ClassScanner scanner,
                              EnumTranslator enumTranslator) {
        this.properties = properties;
        this.groupRepository = groupRepository;
        this.itemRepository = itemRepository;
        this.scanner = scanner;
        this.enumTranslator = enumTranslator;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync() {
        List<String> innerPaths = properties.getInnerPaths();
        if (!CollectionUtils.isEmpty(innerPaths)) {
            innerPaths.forEach(this::syncInternal);
        }

        List<String> excelPaths = properties.getExcelPaths();
        if (!CollectionUtils.isEmpty(excelPaths)) {
            excelPaths.forEach(this::syncExcel);
        }
    }

    private void syncInternal(String basePackage) {
        scanner.scanEnumBy(basePackage).forEach(this::handleEnum);
    }

    private void handleEnum(Class<? extends Enum<?>> enumClass) {
        Preconditions.checkNotNull(enumClass, "enum class == null");

        String simpleName = enumClass.getSimpleName();
        // 枚举类名称是大写开头的驼峰命名，转为带 '_' 连接符的大写名称，作为内置数据字典的 code 关键字
        String code = CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, simpleName);
        // 通过从 message.properties 文件中获取枚举类的国际化内容，如果它不存在则使用枚举类的简称作为字典组名称
        String name = I18nHolder.getAccessor().getMessage(enumClass.getName(), simpleName);

        // 通过 code 找到数据字典组，如果存在就更新，如果不存在就创建
        DictGroup dictGroup = groupRepository.findByCode(code).orElseGet(DictGroup::new);
        // 已设置冻结，不进行更新
        if (Logic.YES.equals(dictGroup.getFreeze())) {
            log.info(I18nHolder.getAccessor().getMessage(
                    "DictService.syncInternal.group.freeze", new Object[]{name, code},
                    Strings.lenientFormat("内置字典组 %s-%s 已冻结，跳过同步", name, code)));
            return;
        }

        dictGroup.setName(name);
        dictGroup.setCode(code);
        dictGroup.setType(DictType.INTERNAL);
        dictGroup.setFreeze(Logic.NO);
        groupRepository.save(dictGroup);

        log.info(I18nHolder.getAccessor().getMessage(
                "DictService.syncInternal.group", new Object[]{name, code},
                Strings.lenientFormat("内置字典组 %s-%s 已同步", name, code)));

        // 以字典项的 value 作为 Map key 避免重复
        Map<String, DictItem> itemMap = Optional.ofNullable(dictGroup.getItems())
                .map(it -> it.stream().collect(Collectors.toMap(DictItem::getValue, Function.identity())))
                .orElse(Maps.newHashMap());
        // 枚举常量不可能有重复值，所以不需要再 put 到 itemMap 中
        for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
            // 通过枚举翻译器（即 rest-messages_[lang].properties 国际化文件）枚举值对应的标签
            // 比如 com.xxx.EnumClassName.ENUM_VALUE
            String label = enumTranslator.asText(enumConstant);
            String itemValue = enumConstant.name();
            if (itemValue.length() > Domains.ENUM_NAME_LENGTH) {
                String defaultMessage = Strings.lenientFormat(
                        "同步失败！内置字典项 %s-%s 名称长度大于指定长度 %s",
                        code, enumConstant.name(), Domains.ENUM_NAME_LENGTH);
                String message = I18nHolder.getAccessor().getMessage("DictService.syncInternal.item.failure", defaultMessage);
                throw new InitializationException(message);
            }
            // 存在即更新，否则就创建
            DictItem dictItem = Optional.ofNullable(itemMap.get(itemValue)).orElseGet(DictItem::new);
            dictItem.setGroup(dictGroup);
            dictItem.setLabel(label);
            dictItem.setValue(itemValue);
            itemRepository.save(dictItem);

            log.info(I18nHolder.getAccessor().getMessage(
                    "DictService.syncInternal.item", new Object[]{label, itemValue, name},
                    Strings.lenientFormat("内置字典项 %s-%s 已同步到 %s 字典组", label, itemValue, name)));
        }
    }

    @SneakyThrows
    private void syncExcel(String excelFile) {
        Preconditions.checkNotNull(excelFile, "dict excel file == null");
        File file = ResourceUtils.getFile(excelFile);
        Preconditions.checkArgument(file.exists(), "dict excel file must be exists");
        Preconditions.checkArgument(!file.isDirectory(), "dict excel file must be not directory");

        // WorkbookFactory 支持创建 HSSFWorkbook 和 XSSFWorkbook 实例
        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet group = workbook.getSheet(GROUP_SHEET_NAME);
            if (group == null) {
                log.warn(I18nHolder.getAccessor().getMessage(
                        "DictService.importExcel.notFound", new Object[]{GROUP_SHEET_NAME},
                        Strings.lenientFormat("未找到名为 %s 的 Sheet 页", GROUP_SHEET_NAME)));
                return;
            }

            // 尝试处理字典组，生成相关实体，并返回以名称为 key 的映射
            Map<String, DictGroup> groupMap = attemptHandleGroup(group);
            if (groupMap.isEmpty()) {
                String invalidMessage = I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.invalid", new Object[]{excelFile},
                        Strings.lenientFormat("Excel 文件 %s 不存在有效字典组数据", excelFile));
                throw new RuntimeException(invalidMessage);
            }

            Sheet item = workbook.getSheet(ITEM_SHEET_NAME);
            if (item == null) {
                log.warn(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.notFound", new Object[]{ITEM_SHEET_NAME},
                        Strings.lenientFormat("未找到名为 %s 的 Sheet 页", ITEM_SHEET_NAME)));
                return;
            }

            attemptHandleItem(groupMap, item);
        } catch (IOException e) {
            String exceptionMessage = I18nHolder.getAccessor().getMessage(
                    "DictService.syncExcel.exception", new Object[]{excelFile},
                    Strings.lenientFormat("读取 Excel 文件 %s 出错", excelFile));
            throw new RuntimeException(exceptionMessage, e);
        }
    }

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
                log.info(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.empty.name", new Object[]{cells.getRowNum()},
                        Strings.lenientFormat(
                                "发现第 %s 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                break;
            }

            String code = Cells.ofString(cells.getCell(1));
            if (Strings.isNullOrEmpty(code)) {
                log.info(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.empty.code", new Object[]{cells.getRowNum()},
                        Strings.lenientFormat(
                                "发现第 %s 行 code 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                break;
            }

            DictGroup entity = groupRepository.findByCode(code).orElseGet(DictGroup::new);
            entity.setName(name);
            entity.setCode(code);
            entity.setType(DictType.EXCEL);
            entity.setFreeze(Logic.NO);
            // save 方法本身带有事务，然后当前 service public 方法也带有事务
            // 根据 REQUIRED 传播类型，则 save 会自动加入 service 的当前事务
            groupMap.put(code, groupRepository.save(entity));

            log.info(I18nHolder.getAccessor().getMessage(
                    "DictService.syncExcel.group", new Object[]{name, code},
                    Strings.lenientFormat("Excel 字典组 %s-%s 已同步", name, code)));
        }
        return groupMap;
    }

    private void attemptHandleItem(Map<String, DictGroup> groupMap, Sheet item) {
        boolean skipHeader = true;
        for (Row cells : item) {
            if (skipHeader) {
                skipHeader = false;
                continue;
            }

            String code = Cells.ofString(cells.getCell(0));
            if (Strings.isNullOrEmpty(code)) {
                log.info(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.empty.code", new Object[]{cells.getRowNum()},
                        Strings.lenientFormat(
                                "发现第 %s 行 code 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                break;
            }

            DictGroup dictGroup = groupMap.get(code);
            if (dictGroup == null) {
                log.warn(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.code.notFound", new Object[]{code},
                        Strings.lenientFormat("错误的字典项，指定的 code %s 在 group Sheet 页中不存在", code)));
                continue;
            }

            String label = Cells.ofString(cells.getCell(1));
            if (Strings.isNullOrEmpty(label)) {
                log.info(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.empty.label", new Object[]{cells.getRowNum()},
                        Strings.lenientFormat(
                                "发现第 %s 行 label 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                break;
            }

            String value = Cells.ofString(cells.getCell(2));
            if (Strings.isNullOrEmpty(value)) {
                log.info(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.empty.value", new Object[]{cells.getRowNum()},
                        Strings.lenientFormat(
                                "发现第 %s 行 value 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                break;
            }

            DictItem entity = itemRepository.findByGroup_CodeAndValue(code, value).orElseGet(DictItem::new);
            entity.setLabel(label);
            entity.setValue(value);
            entity.setGroup(dictGroup);
            itemRepository.save(entity);

            log.info(I18nHolder.getAccessor().getMessage(
                    "DictService.syncExcel.item", new Object[]{label, value, code},
                    Strings.lenientFormat("Excel 字典项 %s-%s 已同步到 %s 字典组", label, value, code)));
        }
    }
}
