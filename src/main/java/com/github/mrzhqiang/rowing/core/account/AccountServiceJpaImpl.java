package com.github.mrzhqiang.rowing.core.account;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class AccountServiceJpaImpl implements AccountService {

    private final AccountMapper mapper;
    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final MessageSourceAccessor accessor;

    public AccountServiceJpaImpl(AccountMapper mapper,
                                 AccountRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 MessageSource messageSource) {
        this.mapper = mapper;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.accessor = new MessageSourceAccessor(messageSource);
    }

    @Override
    public Account loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(accessor.getMessage("UsernameNotFoundException")));
    }

    @Override
    public Account initAdmin() {
        return repository.findByUsername(ADMIN_USERNAME).orElseGet(this::createAdmin);
    }

    private Account createAdmin() {
        Account admin = new Account();
        admin.setUsername(ADMIN_USERNAME);
        String password = UUID.randomUUID().toString();
        log.info("create admin account generate random password {}", password);
        admin.setPassword(passwordEncoder.encode(password));

        return admin;
    }

    @Override
    public boolean register(RegisterForm form) {
        Account entity = mapper.toEntity(form);
        String username = entity.getUsername();
        if (repository.findByUsername(username).isPresent()) {
            if (log.isDebugEnabled()) {
                log.debug("Username {} already exists for register", username);
            }
            return false;
        }

        Account account = new Account();
        account.setUsername(username);
        String encodePassword = passwordEncoder.encode(entity.getPassword());
        account.setPassword(encodePassword);
        repository.save(account);
        if (log.isDebugEnabled()) {
            log.debug("Created account: {} for register", account);
        }
        return true;
    }
}
