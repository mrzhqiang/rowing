package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.kaptcha.autoconfigure.KaptchaAuthenticationConverter;
import com.github.mrzhqiang.kaptcha.autoconfigure.KaptchaProperties;
import com.github.mrzhqiang.rowing.core.account.LoginFailureHandler;
import com.github.mrzhqiang.rowing.core.domain.Authority;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityConfiguration {

    private final SecurityProperties properties;
    private final KaptchaProperties kaptchaProperties;
    private final KaptchaAuthenticationConverter kaptchaConverter;
    private final AuthenticationConfiguration configuration;
    private final LoginFailureHandler failureHandler;

    public SecurityConfiguration(SecurityProperties properties,
                                 KaptchaProperties kaptchaProperties,
                                 KaptchaAuthenticationConverter kaptchaConverter,
                                 AuthenticationConfiguration configuration,
                                 LoginFailureHandler failureHandler) {
        this.properties = properties;
        this.kaptchaProperties = kaptchaProperties;
        this.kaptchaConverter = kaptchaConverter;
        this.configuration = configuration;
        this.failureHandler = failureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AccessDecisionVoter<?> hierarchyVoter() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(RoleHierarchyUtils.roleHierarchyFromMap(Authority.hierarchy()));
        return new RoleHierarchyVoter(hierarchy);
    }

    @Bean
    public WebSecurityCustomizer ignoring() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers(properties.getIgnorePath());
    }

    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        AuthenticationManager manager = configuration.getAuthenticationManager();

        AuthenticationFilter kaptchaFilter = new AuthenticationFilter(manager, kaptchaConverter);
        kaptchaFilter.setRequestMatcher(new AntPathRequestMatcher(properties.getRegisterPath(), HttpMethod.POST.name()));
        kaptchaFilter.setFailureHandler(new SimpleUrlAuthenticationFailureHandler(properties.getRegisterFailedPath()));
        http.addFilterAfter(kaptchaFilter, AnonymousAuthenticationFilter.class);

        http.authorizeRequests(registry -> registry
                        .antMatchers(kaptchaProperties.getPath()).permitAll()
                        .antMatchers(properties.getPublicPath()).permitAll()
                        .antMatchers(properties.getLoginPath()).permitAll()
                        .antMatchers(properties.getRegisterPath()).permitAll()
                        .anyRequest().authenticated())
                .formLogin(configurer -> configurer
                        .loginPage(properties.getLoginPath())
                        .defaultSuccessUrl(properties.getDefaultSuccessUrl())
                        .failureHandler(failureHandler).permitAll())
                .logout().permitAll();
        if (properties.getRememberMe()) {
            http.rememberMe();
        }
        return http.build();
    }
}
