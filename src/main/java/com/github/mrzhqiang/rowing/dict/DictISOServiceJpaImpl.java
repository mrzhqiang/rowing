package com.github.mrzhqiang.rowing.dict;

import com.github.mrzhqiang.rowing.config.DictProperties;
import com.github.mrzhqiang.rowing.domain.DictType;
import com.github.mrzhqiang.rowing.domain.Logic;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.util.Cells;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
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

@Slf4j
@Service
public class DictISOServiceJpaImpl implements DictISOService {

    private final DictProperties properties;
    private final DictGroupRepository groupRepository;
    private final DictISO639Repository iso639Repository;
    private final DictISO3166Repository iso3166Repository;

    public DictISOServiceJpaImpl(DictProperties properties,
                                 DictGroupRepository groupRepository,
                                 DictISO639Repository iso639Repository,
                                 DictISO3166Repository iso3166Repository) {
        this.properties = properties;
        this.groupRepository = groupRepository;
        this.iso639Repository = iso639Repository;
        this.iso3166Repository = iso3166Repository;
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

        if (excelFile.contains(Dicts.ISO_639_FILENAME)) {
            if (groupRepository.findByCode(Dicts.ISO_639_FILENAME).isPresent()) {
                log.info("检测到 ISO 639 字典已经存在，跳过更新");
                return;
            }

            syncISO639Excel(excelFile);

            DictGroup entity = new DictGroup();
            entity.setName(Dicts.ISO_639_FILENAME);
            entity.setCode(Dicts.ISO_639_FILENAME);
            entity.setType(DictType.EXCEL);
            entity.setFreeze(Logic.NO);
            groupRepository.save(entity);
            return;
        }
        if (excelFile.contains(Dicts.ISO_3166_FILENAME)) {
            if (groupRepository.findByCode(Dicts.ISO_3166_FILENAME).isPresent()) {
                log.info("检测到 ISO 3166 字典已经存在，跳过更新");
                return;
            }

            syncISO3166Excel(excelFile);

            DictGroup entity = new DictGroup();
            entity.setName(Dicts.ISO_3166_FILENAME);
            entity.setCode(Dicts.ISO_3166_FILENAME);
            entity.setType(DictType.EXCEL);
            entity.setFreeze(Logic.NO);
            groupRepository.save(entity);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @SneakyThrows
    private void syncISO639Excel(String excelFile) {
        Preconditions.checkNotNull(excelFile, "iso 639 excel file == null");
        File file = ResourceUtils.getFile(excelFile);

        Preconditions.checkArgument(file.exists(), "iso 639 excel file must be exists");
        Preconditions.checkArgument(!file.isDirectory(), "iso 639 excel file must be not directory");

        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet wiki = workbook.getSheet(Dicts.WIKI_SHEET_NAME);
            if (wiki == null) {
                log.warn(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.notFound", new Object[]{Dicts.WIKI_SHEET_NAME},
                        Strings.lenientFormat("未找到名为 %s 的 Sheet 页", Dicts.WIKI_SHEET_NAME)));
                return;
            }

            boolean skipHeader = true;
            for (Row cells : wiki) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String family = Cells.ofString(cells.getCell(0));
                String cnName = Cells.ofString(cells.getCell(1));
                String selfName = Cells.ofString(cells.getCell(2));
                String name = Cells.ofString(cells.getCell(3));
                String code = Cells.ofString(cells.getCell(4));
                String notes = Cells.ofString(cells.getCell(5));

                if (Strings.isNullOrEmpty(name)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "DictService.syncExcel.empty.name", new Object[]{cells.getRowNum()},
                            Strings.lenientFormat("发现第 %s 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                if (Strings.isNullOrEmpty(code)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "DictService.syncExcel.empty.code", new Object[]{cells.getRowNum()},
                            Strings.lenientFormat("发现第 %s 行 code 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                DictISO639 entity = new DictISO639();
                entity.setCode(code);
                entity = iso639Repository.findOne(Example.of(entity)).orElse(entity);
                entity.setName(name);
                entity.setFamily(family);
                entity.setCnName(cnName);
                entity.setSelfName(selfName);
                entity.setNotes(notes);
                iso639Repository.save(entity);

                log.info(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.iso-639", new Object[]{name, code},
                        Strings.lenientFormat("Excel ISO 639 %s-%s 已同步", name, code)));
            }
        } catch (IOException cause) {
            String message = I18nHolder.getAccessor().getMessage(
                    "DictService.syncExcel.exception", new Object[]{file.getName()},
                    Strings.lenientFormat("读取 Excel 文件 %s 出错", file.getName()));
            throw new RuntimeException(message, cause);
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @SneakyThrows
    private void syncISO3166Excel(String excelFile) {
        Preconditions.checkNotNull(excelFile, "iso 3166 excel file == null");
        File file = ResourceUtils.getFile(excelFile);

        Preconditions.checkArgument(file.exists(), "iso 3166 excel file must be exists");
        Preconditions.checkArgument(!file.isDirectory(), "iso 3166 excel file must be not directory");

        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet wiki = workbook.getSheet(Dicts.WIKI_SHEET_NAME);
            if (wiki == null) {
                log.warn(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.notFound", new Object[]{Dicts.WIKI_SHEET_NAME},
                        Strings.lenientFormat("未找到名为 %s 的 Sheet 页", Dicts.WIKI_SHEET_NAME)));
                return;
            }

            boolean skipHeader = true;
            for (Row cells : wiki) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String name = Cells.ofString(cells.getCell(0));
                String cnName = Cells.ofString(cells.getCell(1));
                String alpha2Code = Cells.ofString(cells.getCell(2));
                String alpha3Code = Cells.ofString(cells.getCell(3));
                // 对数字代码前置位进行补零
                String numericCode = Strings.padStart(Cells.ofString(cells.getCell(4)), 3, '0');

                if (Strings.isNullOrEmpty(name)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "DictService.syncExcel.empty.name", new Object[]{cells.getRowNum()},
                            Strings.lenientFormat("发现第 %s 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                if (Strings.isNullOrEmpty(alpha2Code)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "DictService.syncExcel.empty.code", new Object[]{cells.getRowNum()},
                            Strings.lenientFormat("发现第 %s 行 code 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                DictISO3166 entity = new DictISO3166();
                entity.setNumericCode(numericCode);
                entity = iso3166Repository.findOne(Example.of(entity)).orElse(entity);
                entity.setName(name);
                entity.setCnName(cnName);
                entity.setAlpha2Code(alpha2Code);
                entity.setAlpha3Code(alpha3Code);
                iso3166Repository.save(entity);

                log.info(I18nHolder.getAccessor().getMessage(
                        "DictService.syncExcel.iso-3166", new Object[]{name, alpha2Code},
                        Strings.lenientFormat("Excel ISO 3166 %s-%s 已同步", name, alpha2Code)));
            }
        } catch (IOException cause) {
            String message = I18nHolder.getAccessor().getMessage(
                    "DictService.syncExcel.exception", new Object[]{file.getName()},
                    Strings.lenientFormat("读取 Excel 文件 %s 出错", file.getName()));
            throw new RuntimeException(message, cause);
        }
    }

}
