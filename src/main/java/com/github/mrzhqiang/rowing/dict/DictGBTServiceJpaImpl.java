package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.config.DictProperties;
import com.github.mrzhqiang.rowing.domain.DictType;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.util.Cells;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DictGBTServiceJpaImpl implements DictGBTService {

    private final DictProperties properties;
    private final DictGroupRepository groupRepository;
    private final DictGBT2260Repository gbt2260Repository;

    public DictGBTServiceJpaImpl(DictProperties properties,
                                 DictGroupRepository groupRepository,
                                 DictGBT2260Repository gbt2260Repository) {
        this.properties = properties;
        this.groupRepository = groupRepository;
        this.gbt2260Repository = gbt2260Repository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sync() {
        List<String> excelPaths = properties.getExcelPaths();
        if (!CollectionUtils.isEmpty(excelPaths)) {
            excelPaths.forEach(this::syncExcel);
        }
    }

    private void syncExcel(String excelFile) {
        Preconditions.checkNotNull(excelFile, "excel file == null");

        if (excelFile.contains(Dicts.GBT_2260_FILENAME)) {
            if (groupRepository.findByCode(Dicts.GBT_2260_FILENAME).isPresent()) {
                log.info("检测到 GB/T 2260 字典已经存在，跳过更新");
                return;
            }

            syncGBT2260Excel(excelFile);

            DictGroup entity = new DictGroup();
            entity.setCode(Dicts.GBT_2260_FILENAME);
            entity.setName(Dicts.GBT_2260_FILENAME);
            entity.setType(DictType.EXCEL);
            entity.setFreeze(Logic.NO);
            groupRepository.save(entity);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @SneakyThrows
    private void syncGBT2260Excel(String excelFile) {
        Preconditions.checkNotNull(excelFile, "gbt 2260 excel file == null");
        File file = ResourceUtils.getFile(excelFile);

        Preconditions.checkArgument(file.exists(), "gbt 2260 excel file must be exists");
        Preconditions.checkArgument(!file.isDirectory(), "gbt 2260 excel file must be not directory");

        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet dataSheet = workbook.getSheet(Dicts.DATA_SHEET_NAME);
            if (dataSheet == null) {
                log.warn(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.notFound", new Object[]{Dicts.DATA_SHEET_NAME},
                        Strings.lenientFormat("未找到名为 %s 的 Sheet 页", Dicts.DATA_SHEET_NAME)));
                return;
            }

            WrapperGBT2260Data data = new WrapperGBT2260Data();

            boolean skipHeader = true;
            for (Row cells : dataSheet) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String code = Cells.ofString(cells.getCell(0));
                String name = Cells.ofString(cells.getCell(1));

                if (Strings.isNullOrEmpty(name)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "DictService.syncExcel.empty.name", new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                if (Strings.isNullOrEmpty(code)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "DictService.syncExcel.empty.code", new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 code 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                DictGBT2260 entity = new DictGBT2260();
                entity.setCode(code);
                entity = gbt2260Repository.findOne(Example.of(entity)).orElse(entity);
                entity.setName(name);
                String level = Dicts.findLevel(code);
                entity.setLevel(level);
                switch (level) {
                    case Dicts.FIRST_LEVEL:
                        data.firstMap.put(code, gbt2260Repository.save(entity));
                        break;
                    case Dicts.SECOND_LEVEL:
                        entity.setParent(data.findParent(code));
                        data.secondMap.put(code, gbt2260Repository.save(entity));
                        break;
                    case Dicts.THIRD_LEVEL:
                        DictGBT2260 parent = data.findParent(code);
                        Dicts.findSummaryCode(code)
                                .filter(it -> !data.summaryMap.containsKey(it))
                                .ifPresent(summaryCode -> {
                                    DictGBT2260 summary = new DictGBT2260();
                                    summary.setCode(summaryCode);
                                    summary.setSummary(true);
                                    summary = gbt2260Repository.findOne(Example.of(summary)).orElse(summary);
                                    summary.setParent(parent);
                                    String summaryName = Dicts.findSummaryName(summaryCode);
                                    if (parent != null) {
                                        summaryName = parent.getName() + summaryName;
                                    }
                                    summary.setName(summaryName);
                                    summary.setLevel(level);
                                    data.summaryMap.put(summaryCode, gbt2260Repository.save(summary));
                                    log.info(I18nHolder.getAccessor().getMessage(
                                            "DictService.syncExcel.gbt-2260.summary",
                                            new Object[]{summaryName, summaryCode},
                                            Strings.lenientFormat("Excel GB/T 2260 汇总码 %s-%s 已同步",
                                                    summaryName, summaryCode)));
                                });
                        entity.setParent(parent);
                        gbt2260Repository.save(entity);
                        break;
                }
                log.info(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.gbt-2260", new Object[]{name, code},
                        Strings.lenientFormat("Excel GB/T 2260 %s-%s 已同步", name, code)));
            }
        } catch (IOException e) {
            String exceptionMessage = I18nHolder.getAccessor().getMessage(
                    "DictService.syncExcel.exception", new Object[]{file.getName()},
                    Strings.lenientFormat("读取 Excel 文件 %s 出错", file.getName()));
            throw new RuntimeException(exceptionMessage, e);
        }
    }

    /**
     * 包装的 GB/T 2260 数据。
     */
    private static final class WrapperGBT2260Data {

        // 省级行政区：共计 34 条数据
        final Map<String, DictGBT2260> firstMap = Maps.newHashMapWithExpectedSize(100);
        // 地级行政区：大约 333 条数据
        final Map<String, DictGBT2260> secondMap = Maps.newHashMapWithExpectedSize(500);
        // 县级行政区：大约 2843 条数据
        //final Map<String, DictGBT2260> thirdMap = Maps.newHashMapWithExpectedSize(4000);
        // 县级行政区相关的汇总码，不会超过地级行政区 + 省级行政区的总和
        final Map<String, DictGBT2260> summaryMap = Maps.newHashMapWithExpectedSize(600);

        DictGBT2260 findParent(String code) {
            String level = Dicts.findLevel(code);
            String parentCode = Dicts.findParentCode(code);
            if (Dicts.SECOND_LEVEL.equals(level)) {
                return firstMap.get(parentCode);
            }
            if (Dicts.THIRD_LEVEL.equals(level)) {
                String parentLevel = Dicts.findLevel(parentCode);
                if (Dicts.FIRST_LEVEL.equals(parentLevel)) {
                    return firstMap.get(parentCode);
                } else if (Dicts.SECOND_LEVEL.equals(parentLevel)) {
                    return secondMap.get(parentCode);
                }
            }
            return null;
        }
    }

}
