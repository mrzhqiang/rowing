package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.kaptcha.autoconfigure.KaptchaAuthenticationConverter;
import com.github.mrzhqiang.kaptcha.autoconfigure.KaptchaProperties;
import com.github.mrzhqiang.rowing.account.LoginFailureHandler;
import com.github.mrzhqiang.rowing.domain.Authority;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 安全配置。
 */
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({RowingSecurityProperties.class})
@Configuration
public class SecurityConfiguration {

    private final RowingSecurityProperties properties;
    private final KaptchaProperties kaptchaProperties;
    private final KaptchaAuthenticationConverter kaptchaConverter;
    private final AuthenticationConfiguration configuration;
    private final LoginFailureHandler failureHandler;

    public SecurityConfiguration(RowingSecurityProperties properties,
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

    //@Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //@Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(RoleHierarchyUtils.roleHierarchyFromMap(Authority.hierarchy()));
        return hierarchy;
    }

    //@Bean
    public RoleHierarchyVoter hierarchyVoter(RoleHierarchy hierarchy) {
        return new RoleHierarchyVoter(hierarchy);
    }

    //@Bean
    public WebSecurityCustomizer ignoring() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers(properties.getIgnorePath());
    }

    //@Bean
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
                        .loginPage(properties.getLoginPath()).permitAll()
                        .defaultSuccessUrl(properties.getDefaultSuccessUrl())
                        .failureHandler(failureHandler).permitAll())
                .logout().permitAll();
        if (properties.getRememberMe()) {
            http.rememberMe();
        }
        return http.csrf().disable().build();
    }
}
