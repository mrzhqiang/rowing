package com.github.mrzhqiang.rowing.core.account;

import com.github.mrzhqiang.rowing.util.Matchers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class JpaAccountService implements AccountService {

    private final AccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public JpaAccountService(AccountRepository repository,
                             PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrUid) throws UsernameNotFoundException {
        // uid 是纯数字账号，username 首位必须是字母
        boolean matchesAllOfNumber = Matchers.PURE_NUMBER.matchesAllOf(usernameOrUid);
        Optional<Account> optionalAccount;
        if (matchesAllOfNumber) {
            optionalAccount = repository.findByUid(usernameOrUid);
        } else {
            optionalAccount = repository.findByUsername(usernameOrUid);
        }
        return optionalAccount
                .map(User::withUserDetails)
                .map(User.UserBuilder::build)
                .orElseThrow(() -> new UsernameNotFoundException("AccountService.usernameNotFound"));
    }

    @Override
    public Account findByUser(UserDetails user) {
        return repository.findByUsername(user.getUsername())
                // 基本上不可能出现，除非数据库无法访问，或者已删除对应 username 的 account 表数据
                .orElseThrow(() -> new RuntimeException("当前会话无法找到对应的账户信息"));
    }

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        Account account = this.findByUser(user);
        if (log.isDebugEnabled()) {
            log.debug("update password: {} to {}", account.getPassword(), newPassword);
        }
        account.setPassword(newPassword);
        repository.save(account);
        return User.withUserDetails(account).build();
    }

    @Override
    public boolean register(AccountForm accountForm) {
        String username = accountForm.getUsername();
        if (repository.findByUsername(username).isPresent()) {
            if (log.isDebugEnabled()) {
                log.debug("Username {} already exists for register", username);
            }
            return false;
        }
        String uid = this.generateUid(username);
        while (repository.findByUid(uid).isPresent()) {
            // 随机生成 uid，避免重复
            uid = this.generateUid(username);
        }
        Account account = new Account();
        account.setUid(uid);
        account.setUsername(username);
        String encodePassword = passwordEncoder.encode(accountForm.getPassword());
        account.setPassword(encodePassword);
        repository.save(account);
        if (log.isDebugEnabled()) {
            log.debug("Created account: {} for register", account);
        }
        return true;
    }
}
