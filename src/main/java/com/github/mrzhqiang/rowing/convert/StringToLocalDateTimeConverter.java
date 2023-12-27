package com.github.mrzhqiang.rowing.convert;

import com.github.mrzhqiang.helper.time.DateTimes;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;

/**
 * 字符串转本地日期时间的转换器。
 * <p>
 * 由于 REST + Repository 的自定义查询方法是 GET 请求，而入参是表单形式的字符串，
 * 当字符串转为本地日期时间对象时，只能使用默认方式解析字符串，此时会导致解析异常。
 * 可以通过前端适配格式来解决问题，但这将导致复杂化，并失去一定的灵活性，
 * 所以我们在这里自定义转换器，通过使用与 JSON 序列化相同的格式，保证统一的风格。
 */
public enum StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    INSTANCE;

    @Override
    public LocalDateTime convert(@Nonnull String source) {
        return LocalDateTime.parse(source, DateTimes.BASIC_FORMATTER);
    }

}
