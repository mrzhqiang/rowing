package com.github.mrzhqiang.rowing.module.dict;

import com.github.mrzhqiang.rowing.domain.ClassScanner;
import com.github.mrzhqiang.rowing.domain.DataDictType;
import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.rest.webmvc.json.EnumTranslator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class DataDictServiceJpaImpl implements DataDictService {

    private final ClassScanner scanner;
    private final EnumTranslator enumTranslator;
    private final MessageSourceAccessor messageSourceAccessor;
    private final DictGroupRepository groupRepository;
    private final DictItemRepository itemRepository;

    public DataDictServiceJpaImpl(ClassScanner scanner,
                                  EnumTranslator enumTranslator,
                                  MessageSource messageSource,
                                  DictGroupRepository groupRepository,
                                  DictItemRepository itemRepository) {
        this.scanner = scanner;
        this.enumTranslator = enumTranslator;
        this.messageSourceAccessor = new MessageSourceAccessor(messageSource);
        this.groupRepository = groupRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public void syncInternal(String basePackage) {
        scanner.scanEnum(basePackage).forEach(this::handleEnum);
    }

    private void handleEnum(Class<? extends Enum<?>> enumClass) {
        Preconditions.checkNotNull(enumClass, "enum class == null");

        String simpleName = enumClass.getSimpleName();
        // 枚举类名称是大写开头的驼峰命名，转为带 '-' 连接符的小写名称，作为内置数据字典的 code 关键字
        String code = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, simpleName);
        // 通过从 message.properties 文件中获取枚举类的国际化内容，如果它不存在则使用枚举类的简称作为字典组名称
        String name = messageSourceAccessor.getMessage(enumClass.getName(), simpleName);

        // 通过 code 找到数据字典组，如果存在就更新，如果不存在就创建
        DictGroup dictGroup = groupRepository.findByCode(code).orElseGet(DictGroup::new);
        dictGroup.setName(name);
        dictGroup.setCode(code);
        dictGroup.setType(DataDictType.INTERNAL);

        if (log.isDebugEnabled()) {
            log.debug("同步数据字典组 {}-{}", name, code);
        }

        groupRepository.save(dictGroup);

        // 将数据字典项的 value （即枚举值）作为 key 键，其本身作为 value 值映射为 Map 对象，方便更新已存在的数据字典项
        Map<String, DictItem> itemMap = Optional.ofNullable(dictGroup.getItems())
                .map(Collection::stream)
                .map(it -> it.collect(Collectors.toMap(DictItem::getValue, Function.identity())))
                .orElseGet(Maps::newHashMap);
        // 枚举常量不可能有重复值，所以不需要再 put 到 itemMap 作为重复检验的数据集
        for (Enum<?> enumConstant : enumClass.getEnumConstants()) {
            // 通过枚举翻译器（即从 rest-message.properties 国际化文件获取）枚举值对应的标签
            // 一般是 com.xxx.EnumClassName.ENUM_VALUE 的格式
            String label = enumTranslator.asText(enumConstant);
            String itemValue = enumConstant.name();
            // 存在即更新，否则就创建
            DictItem dictItem = Optional.ofNullable(itemMap.get(itemValue)).orElseGet(DictItem::new);
            dictItem.setGroup(dictGroup);
            dictItem.setLabel(label);
            dictItem.setValue(itemValue);

            if (log.isDebugEnabled()) {
                log.debug("同步数据字典组 {} 的数据字典项 {}-{}", code, label, itemValue);
            }

            itemRepository.save(dictItem);
        }
    }
}
