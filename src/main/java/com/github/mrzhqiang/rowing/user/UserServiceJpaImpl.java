package com.github.mrzhqiang.rowing.user;

import com.github.mrzhqiang.rowing.account.Account;
import org.springframework.stereotype.Service;

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
    public UserInfoData findByOwner(Account owner) {
        return repository.findByOwner(owner)
                .map(mapper::toData)
                .orElse(null);
    }
}
