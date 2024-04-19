package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.helper.Environments;
import com.github.mrzhqiang.helper.random.RandomStrings;
import com.github.mrzhqiang.rowing.action.Action;
import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.domain.ActionType;
import com.github.mrzhqiang.rowing.domain.ThirdUserType;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.role.Role;
import com.github.mrzhqiang.rowing.role.Roles;
import com.github.mrzhqiang.rowing.setting.Setting;
import com.github.mrzhqiang.rowing.setting.SettingService;
import com.github.mrzhqiang.rowing.setting.Settings;
import com.github.mrzhqiang.rowing.util.Authentications;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterCreateEvent;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeCreateEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * 账户服务的 JPA 实现。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceJpaImpl implements AccountService, UserDetailsService {

    private final AccountMapper mapper;
    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final SettingService settingService;

    /**
     * 通过用户名加载用户信息。
     * <p>
     * 这个方法在认证时调用，也就是说，登录时会调用这个方法。
     * <p>
     * 注意：为了保证在 {@link org.springframework.security.core.Authentication} 中的纯粹性，我们返回 {@link User} 实例。
     * <p>
     * 如果需要通过用户名找到账户，请使用 {@link #findByUsername(String)} 方法。
     * <p>
     * 关于 Transactional 只读事务。
     * <p>
     * 是为了在查询到 {@link Account} 时，保证懒加载 {@link Role} 列表时不丢失 {@link org.hibernate.Session} 数据库会话。
     *
     * @param username 用户名。
     * @return 用户详情。
     * @throws UsernameNotFoundException 当无法通过用户名找到用户时，抛出此异常，表示登录失败。
     */
    @Timed
    @Counted
    @RunAsSystem
    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (Authentications.SYSTEM_USERNAME.equals(username)) {
            return User.withUsername(username)
                    // 每次随机密码，避免登录系统虚拟用户
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .authorities(AccountType.ADMIN.getAuthority())
                    .build();
        }

        return repository.findByUsername(username)
                .map(it -> User.builder()
                        .username(it.getUsername())
                        .password(it.getPassword())
                        .authorities(Roles.findAuthorities(it.getRoles()))
                        .accountExpired(Accounts.checkExpired(it))
                        .accountLocked(Accounts.checkLocked(it))
                        .credentialsExpired(Accounts.checkCredentialsExpired(it))
                        .disabled(it.getDisabled())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(
                        I18nHolder.getAccessor().getMessage("AccountService.UsernameNotFoundException")));
    }

    @Timed
    @Counted
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void init() {
        repository.findByUsername(Authentications.ADMIN_USERNAME).orElseGet(this::createAdmin);
    }

    private Account createAdmin() {
        String password = UUID.randomUUID().toString();
        log.info(I18nHolder.getAccessor().getMessage(
                "AccountService.initAdmin.password", new Object[]{password},
                Strings.lenientFormat("创建系统管理员账户并随机生成密码: %s", password)));
        writePasswordFile(password);
        Account admin = Account.builder()
                .username(Authentications.ADMIN_USERNAME)
                .password(passwordEncoder.encode(password))
                .type(AccountType.ADMIN)
                .build();
        // 创建系统管理员账户，不发布 BeforeCreateEvent 事件，避免校验逻辑冲突
        repository.save(admin);
        eventPublisher.publishEvent(new AfterCreateEvent(admin));
        return admin;
    }

    private void writePasswordFile(String password) {
        Path passwordFile = Environments.getUserDir().toPath().resolve(Accounts.SYSTEM_ADMIN_PASSWORD_FILENAME);
        try {
            Files.write(passwordFile, password.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        } catch (IOException e) {
            String message = I18nHolder.getAccessor().getMessage(
                    "AccountService.initAdmin.writeFileFailure", "生成系统管理员的密码文件失败");
            throw new RuntimeException(message, e);
        }
    }

    @Timed
    @Counted
    @Override
    public Optional<Account> findByUsername(String username) {
        if (Authentications.SYSTEM_USERNAME.equals(username)) {
            return Optional.empty();
        }
        return repository.findByUsername(username);
    }

    @Override
    public void handleLoginSuccess(Authentication authentication) {
        repository.findByUsername(authentication.getName())
                .filter(Accounts::checkLocked)
                .map(this::resetNormalAccount)
                .ifPresent(repository::save);
    }

    private Account resetNormalAccount(Account account) {
        account.setFailedCount(0);
        account.setLocked(null);
        return account;
    }

    @Override
    public void handleLoginFailed(Authentication authentication) {
        Authentications.findUsername(authentication)
                .flatMap(repository::findByUsername)
                .map(this::computeFailedCount)
                .ifPresent(repository::save);
    }

    private Account computeFailedCount(Account account) {
        int hasFailedCount = account.getFailedCount();
        if (!Accounts.checkLocked(account)) {
            int maxLoginFailed = settingService.findByCode(Settings.MAX_LOGIN_FAILED)
                    .map(Setting::getContent)
                    .map(Integer::parseInt)
                    .orElse(Settings.DEF_MAX_LOGIN_FAILED);
            if (hasFailedCount < maxLoginFailed) {
                account.setFailedCount(hasFailedCount + 1);
            }
            if (account.getFailedCount() >= maxLoginFailed) {
                Duration duration = settingService.findByCode(Settings.ACCOUNT_LOCKED_DURATION)
                        .map(Setting::getContent)
                        .map(DurationStyle::detectAndParse)
                        .orElse(Settings.DEF_ACCOUNT_LOCKED_DURATION);
                account.setLocked(Instant.now().plus(duration));
            }
            return account;
        }

        // 锁定的账户，如果失败次数未重置为零，则进行重置操作
        if (hasFailedCount != 0) {
            account.setFailedCount(0);
            return account;
        }
        // 返回 null 值不会进行更新操作
        return null;
    }

    @Action(ActionType.REGISTER)
    @Timed
    @Counted
    @RunAsSystem
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(PasswordConfirmForm form) {
        Preconditions.checkNotNull(form, "password confirm form == null");
        Preconditions.checkArgument(form.confirm(), "password confirm failure!");

        Account account = mapper.toEntity(form);
        eventPublisher.publishEvent(new BeforeCreateEvent(account));
        repository.save(account);
        eventPublisher.publishEvent(new AfterCreateEvent(account));
        if (log.isDebugEnabled()) {
            log.debug("Created account: {} for register", account);
        }
    }

    @RunAsSystem
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Optional<Account> register(StudentInfoForm form) {
        Preconditions.checkNotNull(form, "student info form == null");

        String number = form.getNumber();
       /* if (studentAccountRepository.existsByNumber(number)) {
            if (log.isDebugEnabled()) {
                log.debug("student number {} already exists for register", number);
            }
            return Optional.empty();
        }*/

        Account account = new Account();
        String username = generateUsername(number, ThirdUserType.STUDENT.getPrefix());
        account.setUsername(username);
        // 身份证后 6 位作为密码
        String idCard = form.getIdCard();
        String password = idCard.substring(idCard.length() - 6);
        account.setPassword(passwordEncoder.encode(password));
        eventPublisher.publishEvent(new BeforeCreateEvent(account));
        Optional<Account> optionalAccount = Optional.of(repository.save(account));
        eventPublisher.publishEvent(new AfterCreateEvent(account));
        return optionalAccount;
    }

    @RunAsSystem
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Optional<Account> register(TeacherInfoForm form) {
        Preconditions.checkNotNull(form, "teacher info form == null");

        String number = form.getNumber();
        /*if (teacherAccountRepository.existsByNumber(number)) {
            if (log.isDebugEnabled()) {
                log.debug("teacher number {} already exists for register", number);
            }
            return Optional.empty();
        }*/

        Account account = new Account();
        String username = generateUsername(number, ThirdUserType.TEACHER.getPrefix());
        account.setUsername(username);
        // 身份证后 6 位作为密码
        String idCard = form.getIdCard();
        String password = idCard.substring(idCard.length() - 6);
        account.setPassword(passwordEncoder.encode(password));
        eventPublisher.publishEvent(new BeforeCreateEvent(account));
        Optional<Account> optionalAccount = Optional.of(repository.save(account));
        eventPublisher.publishEvent(new AfterCreateEvent(account));
        return optionalAccount;
    }

    private String generateUsername(String number, String prefix) {
        // 中缀学号 hash code 后取模 10000 即取后四位
        int infix = Math.floorMod(number.hashCode(), 10000);
        // 后缀 12 -- 16 个随机字符
        String suffix = RandomStrings.ofLength(12, 16);
        return prefix + infix + suffix;
    }

    @Timed
    @Counted
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Account account) {
        eventPublisher.publishEvent(new BeforeSaveEvent(account));
        repository.save(account);
        eventPublisher.publishEvent(new AfterSaveEvent(account));
    }

}
