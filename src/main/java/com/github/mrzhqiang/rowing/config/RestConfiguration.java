package com.github.mrzhqiang.rowing.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.github.mrzhqiang.helper.time.DateTimes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import java.time.LocalDateTime;

/**
 * Spring Data REST 配置。
 * <p>
 */
@Configuration
public class RestConfiguration {

    @Bean
    public RepositoryRestConfigurer customRepositoryRestConfigurer() {
        return new RepositoryRestConfigurer() {
            @Override
            public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
                SimpleModule javaTimeModule = new SimpleModule();
                javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimes.BASIC_FORMATTER));
                javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimes.BASIC_FORMATTER));
                objectMapper.registerModule(javaTimeModule);
            }

            @Override
            public void configureConversionService(ConfigurableConversionService conversionService) {
                //conversionService.addConverter(new StringToExampleConverter());
            }
        };
    }
}
