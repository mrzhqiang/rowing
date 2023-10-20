package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.kaptcha.autoconfigure.KaptchaAuthenticationConverter;
import com.github.mrzhqiang.kaptcha.autoconfigure.KaptchaProperties;
import com.github.mrzhqiang.rowing.account.LoginFailureHandler;
import com.github.mrzhqiang.rowing.account.LoginSuccessHandler;
import com.github.mrzhqiang.rowing.account.RSADecryptPasswordEncoder;
import com.github.mrzhqiang.rowing.setting.SettingService;
import com.github.mrzhqiang.rowing.util.Authorizes;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * 安全配置。
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties({SecurityProperties.class})
@Configuration
public class SecurityConfiguration {

    private final SecurityProperties securityProperties;
    private final SessionProperties sessionProperties;

    public SecurityConfiguration(SecurityProperties securityProperties,
                                 SessionProperties sessionProperties) {
        this.securityProperties = securityProperties;
        this.sessionProperties = sessionProperties;
    }

    /**
     * 自定义密码编码器。
     * <p>
     * 用于对密码进行解密处理。
     */
    @Bean
    public PasswordEncoder passwordEncoder(SettingService settingService) {
        return new RSADecryptPasswordEncoder(settingService);
    }

    /**
     * 角色层次结构。
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(RoleHierarchyUtils.roleHierarchyFromMap(Authorizes.hierarchy()));
        return hierarchy;
    }

    /**
     * 角色层次结构投票人。
     * <p>
     * 声明这个实例，才能具有层次结构。
     */
    @Bean
    public RoleHierarchyVoter hierarchyVoter(RoleHierarchy hierarchy) {
        return new RoleHierarchyVoter(hierarchy);
    }

    /**
     * 注册验证码过滤器。
     */
    @Bean
    public AuthenticationFilter registerKaptchaFilter(AuthenticationConfiguration configuration,
                                                      KaptchaAuthenticationConverter kaptchaConverter) throws Exception {
        AuthenticationManager manager = configuration.getAuthenticationManager();
        AuthenticationFilter filter = new AuthenticationFilter(manager, kaptchaConverter);
        filter.setRequestMatcher(new AntPathRequestMatcher(securityProperties.getRegisterPath(), HttpMethod.POST.name()));
        return filter;
    }

    /**
     * 登录验证码过滤器。
     */
    @Bean
    public AuthenticationFilter loginKaptchaFilter(AuthenticationConfiguration configuration,
                                                   KaptchaAuthenticationConverter kaptchaConverter) throws Exception {
        AuthenticationManager manager = configuration.getAuthenticationManager();
        AuthenticationFilter filter = new AuthenticationFilter(manager, kaptchaConverter);
        filter.setRequestMatcher(new AntPathRequestMatcher(securityProperties.getLoginPath(), HttpMethod.POST.name()));
        return filter;
    }

    /**
     * 网页安全自定义器。
     */
    @Bean
    public WebSecurityCustomizer ignoring() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .antMatchers(securityProperties.getIgnorePath());
    }

    /**
     * 安全过滤器链。
     */
    @Bean
    public SecurityFilterChain webFilterChain(HttpSecurity http,
                                              AuthenticationFilter registerKaptchaFilter,
                                              AuthenticationFilter loginKaptchaFilter,
                                              KaptchaProperties kaptchaProperties,
                                              LoginFailureHandler loginFailureHandler,
                                              LoginSuccessHandler loginSuccessHandler) throws Exception {
        return http.addFilterAfter(registerKaptchaFilter, AnonymousAuthenticationFilter.class)
                .addFilterAfter(loginKaptchaFilter, AnonymousAuthenticationFilter.class)
                .authorizeRequests(urlRegistry -> urlRegistry
                        .antMatchers(HttpMethod.GET, kaptchaProperties.getPath()).permitAll()
                        .antMatchers(HttpMethod.POST, securityProperties.getRegisterPath()).permitAll()
                        .antMatchers(HttpMethod.POST, securityProperties.getLoginPath()).permitAll()
                        .antMatchers(HttpMethod.POST, securityProperties.getLogoutPath()).permitAll()
                        .antMatchers(securityProperties.getPublicPath()).permitAll()
                        .anyRequest().authenticated())
                .formLogin(loginConfigurer -> loginConfigurer
                        .loginPage(securityProperties.getLoginPath())
                        .failureHandler(loginFailureHandler)
                        .successHandler(loginSuccessHandler))
                .exceptionHandling(handlingConfigurer -> handlingConfigurer
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(managementConfigurer -> managementConfigurer
                        .invalidSessionStrategy((request, response) ->
                                response.sendError(HttpStatus.FORBIDDEN.value(), "invalidSessionStrategy"))
                        .sessionConcurrency(controlConfigurer -> controlConfigurer
                                .maximumSessions(sessionProperties.getMaxSession())
                                .expiredSessionStrategy(event ->
                                        event.getResponse().sendError(HttpStatus.FORBIDDEN.value(), "expiredSessionStrategy"))))
                .logout(logoutConfigurer -> logoutConfigurer
                        .logoutUrl(securityProperties.getLogoutPath())
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .deleteCookies("JSESSIONID"))
                .csrf().disable()
                .build();
    }

}
