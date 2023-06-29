package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.SettingTab;
import com.github.mrzhqiang.rowing.domain.SettingType;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.util.Cells;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class SettingServiceJpaImpl implements SettingService {

    private final SettingMapper mapper;
    private final SettingRepository repository;

    public SettingServiceJpaImpl(SettingMapper mapper,
                                 SettingRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public void importExcel(File excelFile) {
        Preconditions.checkNotNull(excelFile, "excel file == null");
        Preconditions.checkArgument(excelFile.exists(), "excel file must be exists");
        Preconditions.checkArgument(!excelFile.isDirectory(), "excel file must be not directory");

        // WorkbookFactory 支持创建 HSSFWorkbook 和 XSSFWorkbook 实例
        try (Workbook workbook = WorkbookFactory.create(excelFile)) {
            Sheet sheet = workbook.getSheet(SHEET_NAME);
            if (sheet == null) {
                String notFoundMessage = I18nHolder.getAccessor().getMessage("SettingService.importExcel.notFound",
                        new Object[]{SHEET_NAME},
                        Strings.lenientFormat("未找到名为 %s 的 Sheet 页", SHEET_NAME));
                log.warn(notFoundMessage);
                return;
            }

            boolean skipHeader = true;
            for (Row cells : sheet) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String label = Cells.ofString(cells.getCell(0));
                if (Strings.isNullOrEmpty(label)) {
                    String emptyNameMessage = I18nHolder.getAccessor().getMessage("SettingService.importExcel.empty.label",
                            new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 label 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                    log.info(emptyNameMessage);
                    break;
                }

                String type = Cells.ofString(cells.getCell(1));
                if (Strings.isNullOrEmpty(type)) {
                    String emptyNameMessage = I18nHolder.getAccessor().getMessage("SettingService.importExcel.empty.type",
                            new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 type 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                    log.info(emptyNameMessage);
                    break;
                }

                String name = Cells.ofString(cells.getCell(2));
                if (Strings.isNullOrEmpty(name)) {
                    String emptyNameMessage = I18nHolder.getAccessor().getMessage("SettingService.importExcel.empty.name",
                            new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                    log.info(emptyNameMessage);
                    break;
                }

                String content = Cells.ofString(cells.getCell(3));
                if (Strings.isNullOrEmpty(content)) {
                    String emptyNameMessage = I18nHolder.getAccessor().getMessage("SettingService.importExcel.empty.content",
                            new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 content 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                    log.info(emptyNameMessage);
                    break;
                }

                String tab = Cells.ofString(cells.getCell(4));
                if (Strings.isNullOrEmpty(tab)) {
                    String emptyNameMessage = I18nHolder.getAccessor().getMessage("SettingService.importExcel.empty.tab",
                            new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 tab 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                    log.info(emptyNameMessage);
                    break;
                }

                Setting entity = new Setting();
                entity.setName(name);
                // Example 表示查询示例，只有匹配传入参数的非空属性，才返回对应数据
                entity = repository.findOne(Example.of(entity)).orElse(entity);
                entity.setType(SettingType.of(type));
                entity.setLabel(label);
                entity.setContent(content);
                entity.setTab(SettingTab.of(tab));
                repository.save(entity);
            }
        } catch (IOException e) {
            String exceptionMessage = I18nHolder.getAccessor().getMessage("SettingService.importExcel.exception",
                    new Object[]{excelFile},
                    Strings.lenientFormat("读取 Excel 文件 %s 出错", excelFile));
            throw new RuntimeException(exceptionMessage, e);
        }
    }

}
