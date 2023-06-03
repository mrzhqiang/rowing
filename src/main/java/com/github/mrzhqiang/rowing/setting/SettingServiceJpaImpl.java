package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.domain.SettingGroup;
import com.github.mrzhqiang.rowing.domain.SettingType;
import com.github.mrzhqiang.rowing.util.Cells;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class SettingServiceJpaImpl implements SettingService {

    private final SettingMapper mapper;
    private final SettingRepository repository;
    private final MessageSourceAccessor sourceAccessor;

    public SettingServiceJpaImpl(SettingMapper mapper,
                                 SettingRepository repository,
                                 MessageSource messageSource) {
        this.mapper = mapper;
        this.repository = repository;
        this.sourceAccessor = new MessageSourceAccessor(messageSource);
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
                String notFoundMessage = sourceAccessor.getMessage("SettingService.importExcel.notFound",
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
                    String emptyNameMessage = sourceAccessor.getMessage("SettingService.importExcel.empty.label",
                            new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 label 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                    log.info(emptyNameMessage);
                    break;
                }

                String type = Cells.ofString(cells.getCell(1));
                if (Strings.isNullOrEmpty(type)) {
                    String emptyNameMessage = sourceAccessor.getMessage("SettingService.importExcel.empty.type",
                            new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 type 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                    log.info(emptyNameMessage);
                    break;
                }

                String name = Cells.ofString(cells.getCell(2));
                if (Strings.isNullOrEmpty(name)) {
                    String emptyNameMessage = sourceAccessor.getMessage("SettingService.importExcel.empty.name",
                            new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                    log.info(emptyNameMessage);
                    break;
                }

                String value = Cells.ofString(cells.getCell(3));
                if (Strings.isNullOrEmpty(value)) {
                    String emptyNameMessage = sourceAccessor.getMessage("SettingService.importExcel.empty.value",
                            new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 value 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                    log.info(emptyNameMessage);
                    break;
                }

                String group = Cells.ofString(cells.getCell(4));
                if (Strings.isNullOrEmpty(group)) {
                    String emptyNameMessage = sourceAccessor.getMessage("SettingService.importExcel.empty.group",
                            new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 group 列存在空字符串，判断为结束行，终止解析", cells.getRowNum()));
                    log.info(emptyNameMessage);
                    break;
                }

                Setting entity = new Setting();
                entity.setType(SettingType.of(type));
                entity.setName(name);
                entity.setGroup(SettingGroup.of(group));

                // Example 表示查询示例，只有匹配传入参数的非空属性，才返回对应数据
                entity = repository.findOne(Example.of(entity)).orElse(entity);
                entity.setLabel(label);
                entity.setValue(value);
                repository.save(entity);
            }
        } catch (IOException e) {
            String exceptionMessage = sourceAccessor.getMessage("SettingService.importExcel.exception",
                    new Object[]{excelFile},
                    Strings.lenientFormat("读取 Excel 文件 %s 出错", excelFile));
            throw new RuntimeException(exceptionMessage, e);
        }
    }

}
