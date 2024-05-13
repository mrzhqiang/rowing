package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.kaptcha.autoconfigure.KaptchaAuthenticationConverter;
import com.github.mrzhqiang.kaptcha.autoconfigure.KaptchaProperties;
import com.github.mrzhqiang.rowing.account.LoginFailureHandler;
import com.github.mrzhqiang.rowing.account.LoginSuccessHandler;
import com.github.mrzhqiang.rowing.account.LogoutActionHandler;
import com.github.mrzhqiang.rowing.account.RSADecryptPasswordEncoder;
import com.github.mrzhqiang.rowing.account.RegisterFailureHandler;
import static com.github.mrzhqiang.rowing.config.RowingSecurityProperties.ADMIN_PATH;
import static com.github.mrzhqiang.rowing.config.RowingSecurityProperties.EXPLORER_PATH;
import static com.github.mrzhqiang.rowing.config.RowingSecurityProperties.SUB_PATH;
import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.setting.SettingService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestProperties;
import static org.springframework.boot.autoconfigure.security.SecurityProperties.BASIC_AUTH_ORDER;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyUtils;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 安全配置。
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final RowingSecurityProperties properties;
    private final RepositoryRestProperties restProperties;
    private final SessionProperties sessionProperties;

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
     * 系统角色层次结构。
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        Map<String, List<String>> map = Maps.newHashMap();
        // ROLE_USER 包含 ROLE_ANONYMOUS 可以访问的内容权限
        map.put(AccountType.USER.getAuthority(), Lists.newArrayList(AccountType.ANONYMOUS.getAuthority()));
        // ROLE_ADMIN 则拥有所有权限
        map.put(AccountType.ADMIN.getAuthority(), Arrays.stream(AccountType.values())
                .filter(it -> !AccountType.ADMIN.equals(it))
                .map(AccountType::getAuthority)
                .collect(Collectors.toList()));
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(RoleHierarchyUtils.roleHierarchyFromMap(map));
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
                                                      RegisterFailureHandler registerFailureHandler,
                                                      KaptchaAuthenticationConverter kaptchaConverter) throws Exception {
        AuthenticationManager manager = configuration.getAuthenticationManager();
        AuthenticationFilter filter = new AuthenticationFilter(manager, kaptchaConverter);
        filter.setRequestMatcher(new AntPathRequestMatcher(properties.getRegisterPath(), HttpMethod.POST.name()));
        filter.setFailureHandler(registerFailureHandler);
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
        filter.setRequestMatcher(new AntPathRequestMatcher(properties.getLoginPath(), HttpMethod.POST.name()));
        return filter;
    }

    /**
     * 网页安全自定义器。
     */
    @Bean
    public WebSecurityCustomizer ignoring() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers(properties.getIgnorePath());
    }

    /**
     * 安全过滤器链。
     */
    @Bean
    @Order(BASIC_AUTH_ORDER - 10)
    public SecurityFilterChain webFilterChain(HttpSecurity http,
                                              KaptchaProperties kaptchaProperties,
                                              AuthenticationFilter registerKaptchaFilter,
                                              AuthenticationFilter loginKaptchaFilter,
                                              LoginFailureHandler loginFailureHandler,
                                              LoginSuccessHandler loginSuccessHandler,
                                              LogoutActionHandler logoutActionHandler) throws Exception {
        return http.addFilterAfter(registerKaptchaFilter, AnonymousAuthenticationFilter.class)
                .addFilterAfter(loginKaptchaFilter, AnonymousAuthenticationFilter.class)
                .authorizeRequests(urlRegistry -> urlRegistry
                        .requestMatchers(kaptchaProperties.getPath()).permitAll()
                        .requestMatchers(properties.getPublicPath()).permitAll()
                        .requestMatchers(generateAdminPaths()).hasRole(AccountType.ADMIN.name())
                        .anyRequest().authenticated())
                .formLogin(loginConfigurer -> loginConfigurer
                        .loginPage(properties.getLoginPath())
                        .failureHandler(loginFailureHandler)
                        .successHandler(loginSuccessHandler))
                .exceptionHandling(handlingConfigurer -> handlingConfigurer
                        .accessDeniedHandler(new AccessDeniedHandlerImpl())
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .sessionManagement(managementConfigurer -> managementConfigurer
                        .invalidSessionStrategy((request, response) -> response
                                .sendError(HttpStatus.FORBIDDEN.value(), I18nHolder.getAccessor().getMessage(
                                        "SecurityConfiguration.webFilterChain.invalidSessionStrategy",
                                        "会话已失效，请重新登录")))
                        .sessionConcurrency(controlConfigurer -> controlConfigurer
                                .maximumSessions(sessionProperties.getMaxSession())
                                .expiredSessionStrategy(event -> event.getResponse()
                                        .sendError(HttpStatus.FORBIDDEN.value(), I18nHolder.getAccessor().getMessage(
                                                "SecurityConfiguration.webFilterChain.expiredSessionStrategy",
                                                "会话已过期，请重新登录")))))
                .logout(logoutConfigurer -> logoutConfigurer
                        .addLogoutHandler(logoutActionHandler)
                        .logoutUrl(properties.getLogoutPath())
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                        .deleteCookies("JSESSIONID", "Rowing-Token"))
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    private String[] generateAdminPaths() {
        // /admin/**
        String adminSubPath = ADMIN_PATH + SUB_PATH;
        // /api/explorer
        String restExplorerPath = restProperties.getBasePath() + EXPLORER_PATH;
        // /api/explorer/**
        String restExplorerSubPath = restProperties.getBasePath() + EXPLORER_PATH + SUB_PATH;
        return new String[]{adminSubPath, restExplorerPath, restExplorerSubPath};
    }

    /**
     * 基础认证过滤器链。
     */
    @Bean
    @Order(BASIC_AUTH_ORDER)
    public SecurityFilterChain basicFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(urlRegistry -> urlRegistry
                        .requestMatchers(properties.getBasicPath()).hasRole(RowingSecurityProperties.ROLE_BASIC)
                        .requestMatchers(restProperties.getBasePath()).hasRole(RowingSecurityProperties.ROLE_BASIC))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}
