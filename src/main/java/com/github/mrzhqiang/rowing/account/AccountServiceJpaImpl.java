package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.helper.Environments;
import com.github.mrzhqiang.helper.random.RandomStrings;
import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.domain.ThirdUserType;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.role.RoleRepository;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.rest.core.event.AfterCreateEvent;
import org.springframework.data.rest.core.event.AfterSaveEvent;
import org.springframework.data.rest.core.event.BeforeCreateEvent;
import org.springframework.data.rest.core.event.BeforeSaveEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AccountServiceJpaImpl implements AccountService {

    private final AccountMapper mapper;
    private final AccountRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public AccountServiceJpaImpl(AccountMapper mapper,
                                 AccountRepository repository,
                                 RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder,
                                 ApplicationEventPublisher eventPublisher) {
        this.mapper = mapper;
        this.repository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @RunAsSystem
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (Accounts.SYSTEM_USERNAME.equals(username)) {
            return User.withUsername(username)
                    // 每次随机密码，避免登录系统虚拟用户
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .authorities(AccountType.ADMIN.getAuthority())
                    .build();
        }

        return repository.findByUsername(username)
                .map(User::withUserDetails)
                .map(User.UserBuilder::build)
                .orElseThrow(() -> new UsernameNotFoundException(
                        I18nHolder.getAccessor().getMessage("AccountService.UsernameNotFoundException")));
    }

    @RunAsSystem
    @Override
    public Optional<Account> findByUsername(String username) {
        if (Accounts.SYSTEM_USERNAME.equals(username)) {
            return Optional.empty();
        }
        return repository.findByUsername(username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initAdmin() {
        repository.findByUsername(Accounts.ADMIN_USERNAME).orElseGet(this::createAdmin);
    }

    private Account createAdmin() {
        String password = UUID.randomUUID().toString();
        log.info(I18nHolder.getAccessor().getMessage(
                "AccountService.initAdmin.password", new Object[]{password},
                Strings.lenientFormat("创建超级管理员账户并随机生成密码: %s", password)));
        writePasswordFile(password);
        Account admin = Account.builder()
                .username(Accounts.ADMIN_USERNAME)
                .password(passwordEncoder.encode(password))
                .type(AccountType.ADMIN)
                .authority(AccountType.ADMIN.getAuthority())
                .build();
        repository.save(admin);
        eventPublisher.publishEvent(new AfterCreateEvent(admin));
        return admin;
    }

    private void writePasswordFile(String password) {
        Path passwordFile = Environments.getUserDir().toPath().resolve(Accounts.ADMIN_PASSWORD_FILENAME);
        try {
            Files.write(passwordFile, password.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        } catch (IOException e) {
            String message = I18nHolder.getAccessor().getMessage(
                    "AccountService.initAdmin.writeFileFailure", "生成管理员密码文件失败");
            throw new RuntimeException(message, e);
        }
    }

    @RunAsSystem
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterForm form) {
        Preconditions.checkNotNull(form, "register form == null");

        Account account = mapper.toEntity(form, passwordEncoder);
        eventPublisher.publishEvent(new BeforeCreateEvent(account));
        repository.save(account);
        eventPublisher.publishEvent(new AfterCreateEvent(account));
        if (log.isDebugEnabled()) {
            log.debug("Created account: {} for register", account);
        }
    }

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

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Account account) {
        eventPublisher.publishEvent(new BeforeSaveEvent(account));
        repository.save(account);
        eventPublisher.publishEvent(new AfterSaveEvent(account));
    }

}
