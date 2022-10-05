package com.github.mrzhqiang.rowing.module.account;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 学生账户的 JPA 实现。
 * <p>
 */
@Slf4j
@Service
public class StudentAccountServiceJpaImpl implements StudentAccountService {

    private final StudentAccountMapper mapper;
    private final StudentAccountRepository repository;
    private final AccountService service;

    public StudentAccountServiceJpaImpl(StudentAccountMapper mapper,
                                        StudentAccountRepository repository,
                                        AccountService service) {
        this.mapper = mapper;
        this.repository = repository;
        this.service = service;
    }

    @Override
    public StudentAccount register(StudentInfoForm form) {
        Preconditions.checkNotNull(form, "form == null");

        String number = form.getNumber();
        if (repository.existsByNumber(number)) {
            if (log.isDebugEnabled()) {
                log.debug("student number {} already exists for register", number);
                return null;
            }
        }

        StudentAccount entity = mapper.toEntity(form);

        return null;
    }
}
