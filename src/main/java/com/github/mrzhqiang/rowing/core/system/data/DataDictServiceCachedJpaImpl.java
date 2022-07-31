package com.github.mrzhqiang.rowing.core.system.data;

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
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class DataDictServiceCachedJpaImpl implements DataDictService {

    private final DataDictGroupMapper mapper;
    private final DataDictGroupRepository repository;

    public DataDictServiceCachedJpaImpl(DataDictGroupMapper mapper,
                                        DataDictGroupRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
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

            // 数据字典文件有效，那么我们清理所有旧数据


            Map<String, DataDictGroup> createGroup = attemptCreateGroup(group, excelFile.getName());
            XSSFSheet item = workbook.getSheet("item");
            if (item == null) {
                log.warn("未找到名为 item 的 Sheet 页");
                return createGroup;
            }

            for (Row cells : item) {
                String parent = Cells.ofString(cells.getCell(0));
                String label = Cells.ofString(cells.getCell(1));
                String value = Cells.ofString(cells.getCell(2));

                DataDictGroup dictGroup = createGroup.get(parent);
                if (dictGroup == null) {
                    log.warn("错误的字典项，指定的 parent {} 在 group 中不存在", parent);
                    continue;
                }


            }
            return createGroup;
        } catch (InvalidFormatException | IOException e) {
            String message = Strings.lenientFormat("读取 Excel 文件 %s 出错", excelFile);
            log.error(message, e);
            throw new RuntimeException(e);
        }
    }

    private Map<String, DataDictGroup> attemptCreateGroup(XSSFSheet group, String filename) {
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
