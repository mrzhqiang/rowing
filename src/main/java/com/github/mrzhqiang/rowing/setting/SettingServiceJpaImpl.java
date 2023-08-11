package com.github.mrzhqiang.rowing.setting;

import com.github.mrzhqiang.rowing.account.RunAsSystem;
import com.github.mrzhqiang.rowing.domain.SettingTab;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

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
        importExcel(ResourceUtils.getFile(EXCEL_FILE_LOCATION));
    }

    private void importExcel(File excelFile) {
        Preconditions.checkNotNull(excelFile, "excel file == null");
        Preconditions.checkArgument(excelFile.exists(), "excel file must be exists");
        Preconditions.checkArgument(!excelFile.isDirectory(), "excel file must be not directory");

        // WorkbookFactory 支持创建 HSSFWorkbook 和 XSSFWorkbook 实例
        try (Workbook workbook = WorkbookFactory.create(excelFile)) {
            Sheet sheet = workbook.getSheet(SHEET_NAME);
            if (sheet == null) {
                log.warn(I18nHolder.getAccessor().getMessage(
                        "SettingService.importExcel.notFound", new Object[]{SHEET_NAME},
                        Strings.lenientFormat("未找到名为 %s 的 Sheet 页", SHEET_NAME)));
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
                    log.info(I18nHolder.getAccessor().getMessage(
                            "SettingService.importExcel.empty.label", new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 label 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                String type = Cells.ofString(cells.getCell(1));
                if (Strings.isNullOrEmpty(type)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "SettingService.importExcel.empty.type", new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 type 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                String name = Cells.ofString(cells.getCell(2));
                if (Strings.isNullOrEmpty(name)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "SettingService.importExcel.empty.name", new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 name 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }

                String content = Cells.ofString(cells.getCell(3));
                /*if (Strings.isNullOrEmpty(content)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "SettingService.importExcel.empty.content", new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 content 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
                    break;
                }*/

                String tab = Cells.ofString(cells.getCell(4));
                if (Strings.isNullOrEmpty(tab)) {
                    log.info(I18nHolder.getAccessor().getMessage(
                            "SettingService.importExcel.empty.tab", new Object[]{cells.getRowNum()},
                            Strings.lenientFormat(
                                    "发现第 %s 行 tab 列存在空字符串，判断为结束行，终止解析", cells.getRowNum())));
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
            String exceptionMessage = I18nHolder.getAccessor().getMessage(
                    "SettingService.importExcel.exception", new Object[]{excelFile},
                    Strings.lenientFormat("读取 Excel 文件 %s 出错", excelFile));
            throw new RuntimeException(exceptionMessage, e);
        }
    }

    @Cacheable("setting")
    @RunAsSystem
    @Override
    public Optional<Setting> findByName(String name) {
        return Optional.ofNullable(name).flatMap(repository::findByName);
    }

    @PreAuthorize(Authorizes.HAS_AUTHORITY_ADMIN)
    @Override
    public RSAKeyData createRsaKey() {
        KeyPair keyPair = RSADecrypts.generateKeyPair();
        return new RSAKeyData(RSADecrypts.privateKey(keyPair), RSADecrypts.publicKey(keyPair));
    }
}
