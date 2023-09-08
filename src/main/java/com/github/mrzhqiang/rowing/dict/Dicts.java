package com.github.mrzhqiang.rowing.dict;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 数据字典工具。
 * <p>
 */
public final class Dicts {
    private Dicts() {
        // no instances.
    }

    /**
     * 数据字典文件名称。
     */
    public static final String DICT_FILENAME = "dict";
    /**
     * ISO 639 文件名称。
     */
    public static final String ISO_639_FILENAME = "iso-639";
    /**
     * ISO 3166 文件名称。
     */
    public static final String ISO_3166_FILENAME = "iso-3166";
    /**
     * GB/T 2260 文件名称。
     */
    public static final String GBT_2260_FILENAME = "gbt-2260";
    /**
     * 字典组的 Sheet 名称。
     */
    public static final String GROUP_SHEET_NAME = "group";
    /**
     * 字典项的 Sheet 名称。
     */
    public static final String ITEM_SHEET_NAME = "item";
    /**
     * 维基百科的 Sheet 名称。
     */
    public static final String WIKI_SHEET_NAME = "wiki";
    /**
     * 字典数据的 Sheet 名称。
     */
    public static final String DATA_SHEET_NAME = "data";

    /**
     * 行政区划代码长度。
     */
    public static final int GBT_2260_2007_LENGTH = 6;
    /**
     * 直辖市代码：北京。
     */
    public static final String MUNICIPALITY_BJ_CODE = "110000";
    /**
     * 直辖市代码：天津。
     */
    public static final String MUNICIPALITY_TJ_CODE = "120000";
    /**
     * 直辖市代码：上海。
     */
    public static final String MUNICIPALITY_SH_CODE = "310000";
    /**
     * 直辖市代码：重庆。
     */
    public static final String MUNICIPALITY_CQ_CODE = "500000";
    /**
     * 直辖市代码列表。
     */
    public static final List<String> MUNICIPALITY_CODES = ImmutableList.of(
            MUNICIPALITY_BJ_CODE,
            MUNICIPALITY_TJ_CODE,
            MUNICIPALITY_SH_CODE,
            MUNICIPALITY_CQ_CODE
    );
    /**
     * 台湾省。
     */
    public static final String REGION_TAI_WAN_CODE = "710000";
    /**
     * 特别行政区：香港。
     * <p>
     * SAR = Special Administrative Region
     */
    public static final String SAR_HONG_KONG_CODE = "810000";
    /**
     * 特别行政区：澳门。
     * <p>
     * SAR = Special Administrative Region
     */
    public static final String SAR_MACAO_CODE = "910000";
    /**
     * 台湾省及特区代码列表。
     */
    public static final List<String> REGION_CODES = ImmutableList.of(
            REGION_TAI_WAN_CODE,
            SAR_HONG_KONG_CODE,
            SAR_MACAO_CODE
    );
    /**
     * 需要忽略的 520201 编码。
     * <p>
     * 因为历史原因，贵州省六盘水市的市辖区编码 520201 被钟山区继承。
     */
    public static final String IGNORE_5202_CODE = "520201";
    /**
     * 省级行政区。
     */
    public static final String FIRST_LEVEL = "1";
    /**
     * 省级行政区的正则表达式。
     */
    public static final Pattern FIRST_LEVEL_PATTERN = Pattern.compile("^[0-9]{2}0000$");
    /**
     * 省级行政区代码模板。
     */
    public static final String FIRST_LEVEL_TEMPLATE = "%s0000";
    /**
     * 地级行政区代码模板。
     */
    public static final String SECOND_LEVEL_TEMPLATE = "%s00";
    /**
     * 地级行政区。
     */
    public static final String SECOND_LEVEL = "2";
    /**
     * 地级行政区的正则表达式。
     */
    public static final Pattern SECOND_LEVEL_PATTERN = Pattern.compile("^[0-9]{4}00");
    /**
     * 县级行政区。
     */
    public static final String THIRD_LEVEL = "3";
    /**
     * 直辖市下辖的县级行政区汇总码以及省（自治区）直辖县级行政区汇总码的代码模板。
     */
    public static final String MUNICIPALITY_OR_PROVINCE_COUNTY_SUMMARY_CODE_TEMPLATE = "%s00";
    /**
     * 地级市下辖的县级行政区汇总码模板。
     */
    public static final String PREFECTURE_CITY_SUMMARY_CODE_TEMPLATE = "%s01";
    /**
     * 市辖区。
     * <p>
     * 直辖市下辖的县级行政区，以及地级市下辖的县级行政区。
     */
    public static final String MUNICIPALITY_OR_PREFECTURE_CITY_SUMMARY_NAME = "市辖区";
    /**
     * 县级市。
     * <p>
     * 省（自治区）直辖县级行政区。
     */
    public static final String PROVINCE_COUNTY_SUMMARY_NAME = "县级市";
    /**
     * 直辖市下辖的县级行政区——市辖区及县、自治县代码正则表达式。
     * <p>
     * 市辖区第二层编码：01。
     * <p>
     * 县、自治县第二层编码：02。
     */
    public static final Pattern MUNICIPALITY_CODE_PATTERN =
            Pattern.compile("^(11|12|31|50)(01|02)[0-9]{2}$");
    /**
     * 省（自治区）直辖县级行政区代码正则表达式。
     * <p>
     * 需要优先排除：直辖市、特区以及台湾省代码。
     * <p>
     * 第二层编码：90。
     */
    public static final Pattern PROVINCE_COUNTY_CODE_PATTERN =
            Pattern.compile("^[0-9]{2}90[0-9]{2}$");
    /**
     * 省（自治区）下辖的地级行政区——地级市下辖的县级行政区代码正则表达式。
     * <p>
     * 需要优先排除：直辖市、特区以及台湾省代码。
     * <p>
     * 第二层编码范围：01-20、51-70，第三层编码范围：01-20、51-80。
     */
    public static final Pattern PREFECTURE_CITY_CODE_PATTERN =
            Pattern.compile("^[0-9]{2}(0[1-9]|1[0-9]|20|5[1-9]|6[0-9]|70)(0[1-9]|1[0-9]|20|5[1-9]|[67][0-9]|80)$");

    /**
     * 通过代码找到级别。
     *
     * @param code 代码。通常是符合 GB/T 2260-2007 标准的 6 位行政区划代码。
     * @return 返回级别字符串。
     */
    public static String findLevel(String code) {
        if (ObjectUtils.isEmpty(code)
                || code.length() != GBT_2260_2007_LENGTH) {
            return "";
        }
        if (FIRST_LEVEL_PATTERN.matcher(code).matches()) {
            return FIRST_LEVEL;
        }
        if (SECOND_LEVEL_PATTERN.matcher(code).matches()) {
            return SECOND_LEVEL;
        }
        return THIRD_LEVEL;
    }

    /**
     * 找到上一级代码。
     * <p>
     * 1. 如果是省级代码，则没有上一级。
     * <p>
     * 2. 如果是地级代码，返回省级代码即可。
     * <p>
     * 3. 如果是县级代码，返回地级代码即可。
     * <p>
     * 4. 如果是直辖市管理的县级代码，应该返回对应直辖市代码。
     * <p>
     * 5. 如果是省（自治区）管理的县级代码，应该返回对应省（自治区）代码。
     *
     * @param code 代码。通常是符合 GB/T 2260-2007 标准的 6 位行政区划代码。
     * @return 上一级代码。
     */
    public static String findParentCode(String code) {
        String level = Dicts.findLevel(code);
        switch (level) {
            case FIRST_LEVEL:
                return "";
            case SECOND_LEVEL:
                return Strings.lenientFormat(FIRST_LEVEL_TEMPLATE, code.substring(0, 2));
            case THIRD_LEVEL:
                if (MUNICIPALITY_CODE_PATTERN.matcher(code).matches()
                        || PROVINCE_COUNTY_CODE_PATTERN.matcher(code).matches()) {
                    return Strings.lenientFormat(FIRST_LEVEL_TEMPLATE, code.substring(0, 2));
                }
                return Strings.lenientFormat(SECOND_LEVEL_TEMPLATE, code.substring(0, 4));
        }
        return "";
    }

    /**
     * 通过代码找到汇总码。
     * <p>
     * 如果指定的代码不符合要求，将返回空的汇总码。
     *
     * @param code 代码。通常是符合 GB/T 2260-2007 标准的 6 位行政区划代码。
     * @return 可选的汇总码。
     */
    public static Optional<String> findSummaryCode(String code) {
        if (ObjectUtils.isEmpty(code)
                || code.length() != GBT_2260_2007_LENGTH
                || code.startsWith(IGNORE_5202_CODE.substring(0, 4))
                || REGION_CODES.contains(code)
                || MUNICIPALITY_CODES.contains(code)) {
            return Optional.empty();
        }

        if (MUNICIPALITY_CODE_PATTERN.matcher(code).matches()) {
            return Optional.of(code)
                    .map(it -> it.substring(0, 4))
                    .map(it -> Strings.lenientFormat(MUNICIPALITY_OR_PROVINCE_COUNTY_SUMMARY_CODE_TEMPLATE, it));
        }
        if (PROVINCE_COUNTY_CODE_PATTERN.matcher(code).matches()) {
            return Optional.of(code)
                    .map(it -> it.substring(0, 4))
                    .map(it -> Strings.lenientFormat(MUNICIPALITY_OR_PROVINCE_COUNTY_SUMMARY_CODE_TEMPLATE, it));
        }
        if (PREFECTURE_CITY_CODE_PATTERN.matcher(code).matches()) {
            return Optional.of(code)
                    .map(it -> it.substring(0, 4))
                    .map(it -> Strings.lenientFormat(PREFECTURE_CITY_SUMMARY_CODE_TEMPLATE, it));
        }

        return Optional.empty();
    }

    /**
     * 通过汇总码找到汇总码名称。
     *
     * @param code 汇总码。
     * @return 汇总码名称。
     */
    public static String findSummaryName(String code) {
        return PROVINCE_COUNTY_CODE_PATTERN.matcher(code).matches()
                ? PROVINCE_COUNTY_SUMMARY_NAME
                : MUNICIPALITY_OR_PREFECTURE_CITY_SUMMARY_NAME;
    }

}
