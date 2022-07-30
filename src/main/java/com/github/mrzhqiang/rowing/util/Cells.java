package com.github.mrzhqiang.rowing.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.util.Optional;

/**
 * Apache Poi 的 Cell 工具。
 */
public final class Cells {
    private Cells() {
        // no instances
    }

    private static final DataFormatter DATA_FORMATTER = new DataFormatter();

    /**
     * 从 Cell 中读取一个字符串，保留原来的格式。
     *
     * @param cell 单元格。
     * @return 保留原有格式的字符串。
     */
    public static String ofString(Cell cell) {
        return Optional.ofNullable(cell)
                .map(DATA_FORMATTER::formatCellValue)
                .orElse("");
    }
}
