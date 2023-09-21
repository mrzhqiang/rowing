package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Transactional(rollbackFor = Exception.class)
@Service
public class ExamServiceJpaImpl implements ExamService {

    private final ExamRepository repository;
    private final AccountRepository accountRepository;

    public ExamServiceJpaImpl(ExamRepository repository,
                              AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void updateTakers(Long examId, List<Long> takers) {
        Optional.ofNullable(examId)
                .flatMap(repository::findById)
                .ifPresent(it -> {
                    Exams.validateUpdate(it);
                    if (CollectionUtils.isEmpty(takers)) {
                        it.setTakers(Collections.emptyList());
                    } else {
                        List<Account> accounts = accountRepository.findAllById(takers);
                        it.setTakers(accounts);
                    }
                    // 注意：如果涉及事件处理相关的改动，需要主动发布 BeforeSaveEvent 事件
                    repository.save(it);
                    // 注意：如果涉及事件处理相关的改动，需要主动发布 AfterSaveEvent 事件
                });
    }

    @Override
    public void updateMarkers(Long examId, List<Long> markers) {
        Optional.ofNullable(examId)
                .flatMap(repository::findById)
                .ifPresent(it -> {
                    Exams.validateUpdate(it);
                    if (CollectionUtils.isEmpty(markers)) {
                        it.setMarkers(Collections.emptyList());
                    } else {
                        List<Account> accounts = accountRepository.findAllById(markers);
                        it.setMarkers(accounts);
                    }
                    repository.save(it);
                });
    }

}
