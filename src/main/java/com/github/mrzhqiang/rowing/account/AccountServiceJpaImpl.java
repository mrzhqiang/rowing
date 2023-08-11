package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.helper.Environments;
import com.github.mrzhqiang.helper.random.RandomStrings;
import com.github.mrzhqiang.rowing.domain.AccountType;
import com.github.mrzhqiang.rowing.domain.ThirdUserType;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.github.mrzhqiang.rowing.role.RoleService;
import com.github.mrzhqiang.rowing.user.UserService;
import com.github.mrzhqiang.rowing.util.Authentications;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@Service
public class AccountServiceJpaImpl implements AccountService {

    private final AccountMapper mapper;
    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;

    public AccountServiceJpaImpl(AccountMapper mapper,
                                 AccountRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 UserService userService,
                                 RoleService roleService) {
        this.mapper = mapper;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
    }

    @RunAsSystem
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (Authentications.SYSTEM_USERNAME.equals(username)) {
            return User.withUsername(username)
                    // 每次随机密码，避免登录系统虚拟用户
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .authorities(AccountType.ADMIN.toAuthority())
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
        if (Authentications.SYSTEM_USERNAME.equals(username)) {
            return Optional.of(Account.builder()
                    .username(username)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .type(AccountType.ADMIN)
                    .build());
        }
        return repository.findByUsername(username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void init() {
        Account admin = repository.findByUsername(Authentications.ADMIN_USERNAME).orElseGet(this::createAdminAccount);
        userService.bindingAdmin(admin);
        roleService.bindingAccount(admin);
        repository.save(admin);
    }

    private Account createAdminAccount() {
        Account admin = new Account();
        admin.setUsername(Authentications.ADMIN_USERNAME);
        String password = UUID.randomUUID().toString();
        log.info(I18nHolder.getAccessor().getMessage(
                "AccountService.createAdmin.password", new Object[]{password},
                Strings.lenientFormat("创建 admin 账户并随机生成密码: %s", password)));
        Path passwordFile = Environments.getUserDir().toPath().resolve(ADMIN_PASSWORD_FILENAME);
        try {
            Files.write(passwordFile, password.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(I18nHolder.getAccessor().getMessage("AccountService.createAdmin.writeFileFailure"), e);
        }
        admin.setPassword(passwordEncoder.encode(password));
        admin.setType(AccountType.ADMIN);
        return repository.save(admin);
    }

    @RunAsSystem
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Optional<Account> register(RegisterForm registerForm) {
        Preconditions.checkNotNull(registerForm, "register registerForm == null");
        String username = registerForm.getUsername();
        Preconditions.checkArgument(validUsername(username),
                I18nHolder.getAccessor().getMessage("AccountService.register.invalidUsername"));

        if (repository.existsByUsername(username)) {
            if (log.isDebugEnabled()) {
                log.debug("Username {} already exists when register", username);
            }
            return Optional.empty();
        }

        Account entity = mapper.toEntity(registerForm);
        String encodePassword = passwordEncoder.encode(entity.getPassword());
        entity.setPassword(encodePassword);
        repository.save(entity);
        if (log.isDebugEnabled()) {
            log.debug("Created account: {} for register", entity);
        }
        return Optional.of(entity);
    }

    private boolean validUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        return Stream.of(Authentications.SYSTEM_USERNAME, Authentications.ADMIN_USERNAME)
                .noneMatch(username::equals);
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
        return Optional.of(repository.save(account));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Optional<Account> registerForTeacher(TeacherInfoForm form) {
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
        return Optional.of(repository.save(account));
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
        repository.save(account);
    }
}
