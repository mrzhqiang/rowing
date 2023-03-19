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
                log.warn("未找到名为 {} 的 Sheet 页", SHEET_NAME);
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
                    log.warn("发现第 {} 行 label 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                    break;
                }

                String type = Cells.ofString(cells.getCell(1));
                if (Strings.isNullOrEmpty(type)) {
                    log.warn("发现第 {} 行 type 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
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

                String group = Cells.ofString(cells.getCell(4));
                if (Strings.isNullOrEmpty(group)) {
                    log.warn("发现第 {} 行 group 列包含空字符串，判断为结束行，终止解析", cells.getRowNum());
                    break;
                }

                Setting entity = new Setting();
                entity.setType(SettingType.of(type));
                entity.setName(name);
                entity.setGroup(SettingGroup.of(group));

                entity = repository.findOne(Example.of(entity)).orElse(entity);
                entity.setLabel(label);
                entity.setValue(value);
                repository.save(entity);
            }
        } catch (IOException e) {
            String message = Strings.lenientFormat("读取 Excel 文件 %s 出错", excelFile);
            throw new RuntimeException(message, e);
        }
    }

}
