package com.github.mrzhqiang.rowing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.mrzhqiang.helper.time.DateTimes;
import com.github.mrzhqiang.rowing.convert.StringToLocalDateTimeConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.json.EnumTranslator;
import org.springframework.data.rest.webmvc.json.JacksonSerializers;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;

/**
 * Spring Data REST 配置。
 * <p>
 */
@Configuration
@RequiredArgsConstructor
public class SpringDataRestConfiguration {

    private final RepositoryRestProperties properties;
    private final EnumTranslator enumTranslator;

    @Bean
    public RepositoryRestConfigurer customRepositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureConversionService(ConfigurableConversionService conversionService) {
                // StringToExampleConverter 行不通，仅注释而不删除，是为了警惕不要再犯类似错误
                //conversionService.addConverter(new StringToExampleConverter());
                conversionService.addConverter(StringToLocalDateTimeConverter.INSTANCE);
            }
        };
    }

    @Bean
    public ObjectMapper customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        ObjectMapper mapper = jacksonObjectMapperBuilder.build();
        // 让 MVC 的 JSON 序列化也支持枚举翻译器
        if (Boolean.TRUE.equals(properties.getEnableEnumTranslation())) {
            mapper.registerModule(new JacksonSerializers(enumTranslator));
        }
        SimpleModule javaTimeModule = new SimpleModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimes.BASIC_FORMATTER));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimes.BASIC_FORMATTER));
        mapper.registerModule(javaTimeModule);
        return mapper;
    }

}
