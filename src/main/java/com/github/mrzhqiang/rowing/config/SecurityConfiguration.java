package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.kaptcha.autoconfigure.KaptchaAuthenticationConverter;
import com.github.mrzhqiang.kaptcha.autoconfigure.KaptchaProperties;
import com.github.mrzhqiang.rowing.account.LoginFailureHandler;
import com.github.mrzhqiang.rowing.domain.Authority;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 安全配置。
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({RowingSecurityProperties.class})
@Configuration
public class SecurityConfiguration {

    private final RowingSecurityProperties properties;

    public SecurityConfiguration(RowingSecurityProperties properties) {
        this.properties = properties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(RoleHierarchyUtils.roleHierarchyFromMap(Authority.hierarchy()));
        return hierarchy;
    }

    @Bean
    public RoleHierarchyVoter hierarchyVoter(RoleHierarchy hierarchy) {
        // 声明这个实例，才能具有角色等级制度
        return new RoleHierarchyVoter(hierarchy);
    }

    @Bean
    public AuthenticationFilter registerKaptchaFilter(AuthenticationConfiguration configuration,
                                                      KaptchaAuthenticationConverter kaptchaConverter) throws Exception {
        AuthenticationManager manager = configuration.getAuthenticationManager();
        AuthenticationFilter filter = new AuthenticationFilter(manager, kaptchaConverter);
        filter.setRequestMatcher(new AntPathRequestMatcher(properties.getRegisterPath(), HttpMethod.POST.name()));
        return filter;
    }

    @Bean
    public AuthenticationFilter loginKaptchaFilter(AuthenticationConfiguration configuration,
                                                   KaptchaAuthenticationConverter kaptchaConverter) throws Exception {
        AuthenticationManager manager = configuration.getAuthenticationManager();
        AuthenticationFilter filter = new AuthenticationFilter(manager, kaptchaConverter);
        filter.setRequestMatcher(new AntPathRequestMatcher(properties.getLoginPath(), HttpMethod.POST.name()));
        return filter;
    }

    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http,
                                              AuthenticationFilter registerKaptchaFilter,
                                              AuthenticationFilter loginKaptchaFilter,
                                              KaptchaProperties kaptchaProperties,
                                              LoginFailureHandler failureHandler) throws Exception {
        http.addFilterAfter(loginKaptchaFilter, AnonymousAuthenticationFilter.class);
        http.addFilterAfter(registerKaptchaFilter, AnonymousAuthenticationFilter.class);

        http.authorizeRequests(urlRegistry -> urlRegistry
                        .antMatchers(HttpMethod.GET, kaptchaProperties.getPath()).permitAll()
                        .antMatchers(HttpMethod.POST, properties.getLoginPath(), properties.getRegisterPath()).permitAll()
                        .anyRequest().authenticated())
                .formLogin(loginConfigurer -> loginConfigurer
                        .loginPage(properties.getLoginPath())
                        .failureHandler(failureHandler))
                .logout().permitAll();
        return http.csrf().disable().build();
    }
}
