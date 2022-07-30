package com.github.mrzhqiang.rowing.core.system.data;

import com.github.mrzhqiang.rowing.core.system.exception.ResourceNotFoundException;
import com.github.mrzhqiang.rowing.util.Cells;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CachedJpaDataDictGroupService implements DataDictGroupService {

    private final DataDictGroupMapper mapper;
    private final DataDictGroupRepository repository;

    public CachedJpaDataDictGroupService(DataDictGroupMapper mapper,
                                         DataDictGroupRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public DataDictGroup create(DataDictGroupForm form) {
        return repository.save(mapper.toEntity(form));
    }

    @Override
    public void deleteByCode(String code) {
        repository.deleteByCode(code);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public DataDictGroup update(String code, DataDictGroupForm form) {
        return repository.findByCode(code)
                .map(it -> mapper.update(form, it))
                .map(repository::save)
                .orElseThrow(() -> ResourceNotFoundException.of(
                        Strings.lenientFormat("更新失败！不存在代码为 %s 的数据字典", code)));
    }

    @Override
    public DataDictGroupData findByCode(String code) {
        return repository.findByCode(code)
                .map(mapper::toData)
                .orElseThrow(() -> ResourceNotFoundException.of(
                        Strings.lenientFormat("找不到代码为 %s 的数据字典", code)));
    }

    @Override
    public List<DataDictGroupData> list() {
        return repository.findAll().stream()
                .map(mapper::toData)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DataDictGroupData> page(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toData);
    }

    @Override
    public Map<String, DataDictGroup> importExcel(File excelFile) {
        Preconditions.checkNotNull(excelFile, "excel file == null");
        Preconditions.checkArgument(excelFile.exists(), "excel file must be exists");
        Preconditions.checkArgument(!excelFile.isDirectory(), "excel file must be not directory");

        // 以只读模式打开数据字典 Excel 文件
        try (XSSFWorkbook workbook = new XSSFWorkbook(OPCPackage.open(excelFile, PackageAccess.READ))) {
            XSSFSheet group = workbook.getSheet("group");
            if (group == null) {
                log.warn("未找到名为 group 的 Sheet 页");
                return Collections.emptyMap();
            }

            return attemptCraeteGroup(group);
        } catch (InvalidFormatException | IOException e) {
            String message = Strings.lenientFormat("读取 Excel 文件 %s 出错", excelFile);
            log.error(message, e);
            throw new RuntimeException(e);
        }
    }

    private Map<String, DataDictGroup> attemptCraeteGroup(XSSFSheet group) {
        Map<String, DataDictGroup> groupMap = Maps.newHashMapWithExpectedSize(group.getPhysicalNumberOfRows());
        boolean skipCurrentRow = true;
        for (Row cells : group) {
            String name = Cells.ofString(cells.getCell(0));
            String code = Cells.ofString(cells.getCell(1));
            if (skipCurrentRow) {
                // 我们只跳过第一行，因为它是数据字典的标题
                skipCurrentRow = false;
                continue;
            }

            if (Strings.isNullOrEmpty(name)) {
                log.warn("发现第 {} 行数据的 name 列存在空字符串", cells.getRowNum());
                continue;
            }
            if (Strings.isNullOrEmpty(code)) {
                log.warn("发现第 {} 行数据的 code 列存在空字符串", cells.getRowNum());
                continue;
            }

            DataDictGroupForm form = new DataDictGroupForm();
            form.setName(name);
            form.setCode(code);
            DataDictGroup entity = mapper.toEntity(form);
            groupMap.put(code, repository.save(entity));
        }
        return groupMap;
    }
}
