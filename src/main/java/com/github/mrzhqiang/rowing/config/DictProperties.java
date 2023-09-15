package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.rowing.domain.DictType;
import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.ResourceUtils;

import java.util.List;

/**
 * 字典属性。
 */
@Getter
@Setter
@ToString
@ConfigurationProperties("rowing.dict")
public class DictProperties {

    /**
     * 默认的内置字典路径。
     */
    private static final String DEF_INNER_PATH = DictType.class.getPackage().getName();
    /**
     * 默认的 Excel 字典路径。
     */
    private static final String DEF_EXCEL_PATH = ResourceUtils.CLASSPATH_URL_PREFIX + "data/dict.xlsx";
    /**
     * 默认的 ISO 639 代码路径。
     */
    private static final String DEF_ISO_639_PATH = ResourceUtils.CLASSPATH_URL_PREFIX + "data/iso-639-1.xlsx";
    /**
     * 默认的 ISO 3166 代码路径。
     */
    private static final String DEF_ISO_3166_PATH = ResourceUtils.CLASSPATH_URL_PREFIX + "data/iso-3166-1.xlsx";
    /**
     * 默认的 GB/T 2260 代码路径。
     */
    private static final String DEF_GBT_2260_PATH = ResourceUtils.CLASSPATH_URL_PREFIX + "data/gbt-2260-2007.xlsx";

    /**
     * 内置字典的路径列表。
     * <p>
     * 这个路径列表中的元素是包路径，表示需要扫描的枚举类所在包。
     */
    private List<String> innerPaths = ImmutableList.of(DEF_INNER_PATH);
    /**
     * Excel 字典的路径列表。
     * <p>
     * 这个路径列表中的元素是 Excel 文件路径，表示需要解析的 Excel 数据。
     */
    private List<String> excelPaths = ImmutableList.of(
            DEF_EXCEL_PATH,
            DEF_ISO_639_PATH,
            DEF_ISO_3166_PATH,
            DEF_GBT_2260_PATH
    );

}
