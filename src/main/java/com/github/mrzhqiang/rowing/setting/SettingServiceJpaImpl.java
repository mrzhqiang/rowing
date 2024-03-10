package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.account.RunAsSystem;
import com.github.mrzhqiang.rowing.domain.SettingType;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.util.Authorizes;
import com.github.mrzhqiang.rowing.util.Cells;
import com.github.mrzhqiang.rowing.util.RSADecrypts;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Optional;

@Slf4j
@Service
public class SettingServiceJpaImpl implements SettingService {

    private final SettingRepository repository;

    public SettingServiceJpaImpl(SettingRepository repository) {
        this.repository = repository;
    }

    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void init() {
        importExcel(ResourceUtils.getFile(Settings.EXCEL_FILE_LOCATION));
    }

    private void importExcel(File excelFile) {
        Preconditions.checkNotNull(excelFile, "excel file == null");
        Preconditions.checkArgument(excelFile.exists(), "excel file must be exists");
        Preconditions.checkArgument(!excelFile.isDirectory(), "excel file must be not directory");

        // WorkbookFactory 支持创建 HSSFWorkbook 和 XSSFWorkbook 实例
        try (Workbook workbook = WorkbookFactory.create(excelFile)) {
            Sheet sheet = workbook.getSheet(Settings.EXCEL_FILE_SHEET_NAME);
            if (sheet == null) {
                log.warn(I18nHolder.getAccessor().getMessage(
                        "SettingService.importExcel.notFound", new Object[]{Settings.EXCEL_FILE_SHEET_NAME},
                        Strings.lenientFormat("未找到名为 %s 的 Sheet 页", Settings.EXCEL_FILE_SHEET_NAME)));
                return;
            }

            boolean skipHeader = true;
            for (Row cells : sheet) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String name = Cells.ofString(cells.getCell(0));
                if (Strings.isNullOrEmpty(name)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "SettingService.importExcel.emptyData", new Object[]{cells.getRowNum(), "name"},
                            Strings.lenientFormat(
                                    "发现第 %s 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                String code = Cells.ofString(cells.getCell(1));
                if (Strings.isNullOrEmpty(code)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "SettingService.importExcel.emptyData", new Object[]{cells.getRowNum(), "code"},
                            Strings.lenientFormat(
                                    "发现第 %s 行 code 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                String content = Cells.ofString(cells.getCell(2));
                String type = Cells.ofString(cells.getCell(3));
                if (Strings.isNullOrEmpty(type)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "SettingService.importExcel.emptyData", new Object[]{cells.getRowNum(), "type"},
                            Strings.lenientFormat(
                                    "发现第 %s 行 type 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                Setting entity = new Setting();
                entity.setCode(code);
                entity = repository.findOne(Example.of(entity)).orElse(entity);
                entity.setType(SettingType.of(type));
                entity.setName(name);
                entity.setContent(content);
                repository.save(entity);
            }
        } catch (IOException e) {
            String exceptionMessage = I18nHolder.getAccessor().getMessage(
                    "SettingService.importExcel.exception", new Object[]{excelFile},
                    Strings.lenientFormat("读取 Excel 文件 %s 出错", excelFile));
            throw new RuntimeException(exceptionMessage, e);
        }
    }

    //@Cacheable("setting")
    @RunAsSystem
    @Override
    public Optional<Setting> findByCode(String code) {
        return Optional.ofNullable(code)
                .filter(StringUtils::hasText)
                .map(it -> Example.of(Setting.builder().code(code).build()))
                .flatMap(repository::findOne);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public RSAKeyData createRsaKey() {
        KeyPair keyPair = RSADecrypts.generateKeyPair();
        return new RSAKeyData(RSADecrypts.privateKey(keyPair), RSADecrypts.publicKey(keyPair));
    }

}
