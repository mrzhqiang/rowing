package com.github.mrzhqiang.rowing.system.session;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(SessionProperties.class)
@Configuration
public class SessionConfiguration {
}
