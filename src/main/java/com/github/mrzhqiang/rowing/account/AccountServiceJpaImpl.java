package com.github.mrzhqiang.rowing.account;

import com.github.mrzhqiang.helper.Environments;
import com.github.mrzhqiang.helper.random.RandomStrings;
import com.github.mrzhqiang.rowing.domain.Authority;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

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

    private final SecurityProperties properties;
    private final AccountMapper mapper;
    private final AccountRepository repository;
    private final StudentAccountRepository studentAccountRepository;
    private final TeacherAccountRepository teacherAccountRepository;
    //private final PasswordEncoder passwordEncoder;

    public AccountServiceJpaImpl(SecurityProperties properties,
                                 AccountMapper mapper,
                                 AccountRepository repository,
                                 StudentAccountRepository studentAccountRepository,
                                 TeacherAccountRepository teacherAccountRepository
                                 //PasswordEncoder passwordEncoder,
                                 ) {
        this.properties = properties;
        this.mapper = mapper;
        this.repository = repository;
        this.studentAccountRepository = studentAccountRepository;
        this.teacherAccountRepository = teacherAccountRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    /*@Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 检测是不是系统
        SecurityProperties.User user = properties.getUser();
        if (user.getName().equals(username)) {
            return User.withUsername(username)
                    //.password(passwordEncoder.encode(user.getPassword()))
                    .roles(StringUtils.toStringArray(user.getRoles()))
                    .build();
        }

        // 以学号开头
        if (username.startsWith(Account.STUDENT_ID_PREFIX)) {
            username = username.substring(Account.STUDENT_ID_PREFIX.length());
        }

        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        sourceAccessor.getMessage("AccountService.UsernameNotFoundException")));
    }*/

    @Override
    public Account initAdmin() {
        return repository.findByUsername(ADMIN_USERNAME).orElseGet(this::createAdmin);
    }

    private Account createAdmin() {
        Account admin = new Account();
        admin.setUsername(ADMIN_USERNAME);
        String password = UUID.randomUUID().toString();
        log.info("create admin account generate random password {}", password);
        Path passwordFile = Environments.getUserDir().toPath().resolve(PASSWORD_FILE);
        try {
            Files.write(passwordFile, password.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(I18nHolder.getAccessor().getMessage("AccountService.GenerateAdminFileFailure"), e);
        }
        //admin.setPassword(passwordEncoder.encode(password));

        return admin;
    }

    @Override
    public Optional<Account> register(RegisterForm registerForm) {
        Preconditions.checkNotNull(registerForm, "register registerForm == null");

        String username = registerForm.getUsername();
        if (repository.existsByUsername(username)) {
            if (log.isDebugEnabled()) {
                log.debug("Username {} already exists for register", username);
            }
            return Optional.empty();
        }

        Account entity = mapper.toEntity(registerForm);
        //String encodePassword = passwordEncoder.encode(entity.getPassword());
        //entity.setPassword(encodePassword);
        repository.save(entity);
        if (log.isDebugEnabled()) {
            log.debug("created account: {} for register", entity);
        }
        return Optional.of(entity);
    }

    @Override
    public Optional<Account> register(StudentInfoForm form) {
        Preconditions.checkNotNull(form, "student info form == null");

        String number = form.getNumber();
        if (studentAccountRepository.existsByNumber(number)) {
            if (log.isDebugEnabled()) {
                log.debug("student number {} already exists for register", number);
            }
            return Optional.empty();
        }

        Account account = new Account();
        String username = generateUsername(number, Account.STUDENT_ID_PREFIX);
        account.setUsername(username);
        // 身份证后 6 位作为密码
        String idCard = form.getIdCard();
        String password = idCard.substring(idCard.length() - 6);
        //account.setPassword(passwordEncoder.encode(password));
        // 默认用户角色
        account.setAuthority(Authority.ROLE_USER);
        return Optional.of(repository.save(account));
    }

    @Override
    public Optional<Account> registerForTeacher(TeacherInfoForm form) {
        Preconditions.checkNotNull(form, "teacher info form == null");

        String number = form.getNumber();
        if (teacherAccountRepository.existsByNumber(number)) {
            if (log.isDebugEnabled()) {
                log.debug("teacher number {} already exists for register", number);
            }
            return Optional.empty();
        }

        Account account = new Account();
        String username = generateUsername(number, Account.TEACHER_ID_PREFIX);
        account.setUsername(username);
        // 身份证后 6 位作为密码
        String idCard = form.getIdCard();
        String password = idCard.substring(idCard.length() - 6);
        //account.setPassword(passwordEncoder.encode(password));
        // 默认用户角色
        account.setAuthority(Authority.ROLE_USER);
        return Optional.of(repository.save(account));
    }

    private String generateUsername(String number, String prefix) {
        // 中缀学号 hash code 后取模 10000 即取后四位
        int infix = Math.floorMod(number.hashCode(), 10000);
        // 后缀 12 -- 16 个随机字符
        String suffix = RandomStrings.ofLength(12, 16);
        return prefix + infix + suffix;
    }
}
