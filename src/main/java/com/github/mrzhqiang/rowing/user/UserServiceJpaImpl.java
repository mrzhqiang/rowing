package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.Gender;
import com.github.mrzhqiang.rowing.i18n.I18nHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;

@Service
public class UserServiceJpaImpl implements UserService {

    private final UserMapper mapper;
    private final UserRepository repository;

    public UserServiceJpaImpl(UserMapper mapper,
                              UserRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public UserInfoData findByUsername(String username) {
        return repository.findByOwner_Username(username)
                .map(mapper::toData)
                .orElse(null);
    }

    @Override
    public void createForAdmin(Account admin) {
        repository.findByOwner(admin).orElseGet(() -> createAdminUser(admin));
    }

    private User createAdminUser(Account admin) {
        User user = new User();
        user.setNickname(I18nHolder.getAccessor().getMessage("AccountService.nickname.admin", "管理员"));
        user.setGender(Gender.MALE);
        user.setBirthday(LocalDate.of(2019, Month.of(10), 25));
        user.setIntroduction(I18nHolder.getAccessor().getMessage("AccountService.introduction.admin", "系统管理员。"));
        user.setOwner(admin);
        return repository.save(user);
    }
}
