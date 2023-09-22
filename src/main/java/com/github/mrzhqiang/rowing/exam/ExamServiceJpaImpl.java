package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.account.AccountRepository;
import com.github.mrzhqiang.rowing.account.RunAsSystem;
import com.github.mrzhqiang.rowing.domain.ExamStatus;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceJpaImpl implements ExamService {

    private final ExamRepository repository;
    private final AccountRepository accountRepository;

    public ExamServiceJpaImpl(ExamRepository repository,
                              AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    @Transactional(rollbackFor = Exception.class)
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

    @Transactional(rollbackFor = Exception.class)
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

    @Override
    public Page<ExamInfo> myExamInfo(UserDetails userDetails,
                                     String title,
                                     String code,
                                     LocalDateTime firstStart,
                                     LocalDateTime secondStart,
                                     Pageable pageable) {
        return accountRepository.findByUsername(userDetails.getUsername())
                .map(it -> repository.findAllByTakersContainingAndTitleContainingAndCodeContainingAndStartTimeBetween(
                        it, title, code, firstStart, secondStart, pageable))
                .orElse(Page.empty());
    }

    @Transactional(rollbackFor = Exception.class)
    @RunAsSystem
    @Override
    public void updateStatus() {
        updateRunningFromWaiting();
        updateMarkingFromRunning();
        updateFinishedFromMarking();
    }

    private void updateRunningFromWaiting() {
        Exam probe = new Exam();
        probe.setStatus(ExamStatus.WAITING);
        // TODO 如果存在大量考试，则应该优化这里的逻辑，使用 Example 的匹配策略来查询已开始且未结束的考试
        repository.findAll(Example.of(probe)).stream()
                .filter(it -> LocalDateTime.now().isAfter(it.getStartTime())
                        && LocalDateTime.now().isBefore(it.getEndTime()))
                .peek(it -> it.setStatus(ExamStatus.RUNNING))
                .forEach(repository::save);
    }

    private void updateMarkingFromRunning() {
        Exam probe = new Exam();
        probe.setStatus(ExamStatus.RUNNING);
        // TODO 如果存在大量考试，则应该优化这里的逻辑，使用 Example 的匹配策略来查询已结束的考试
        repository.findAll(Example.of(probe)).stream()
                .filter(it -> LocalDateTime.now().isAfter(it.getEndTime()))
                .peek(it -> it.setStatus(ExamStatus.MARKING))
                .forEach(repository::save);
    }

    private void updateFinishedFromMarking() {
        Exam probe = new Exam();
        probe.setStatus(ExamStatus.MARKING);
        // TODO 如果存在大量考试，则应该优化这里的逻辑，使用 Example 的匹配策略来查询已阅卷完毕的考试
        repository.findAll(Example.of(probe)).stream()
                .filter(it -> it.getPapers().stream().allMatch(ExamPaper::getFinished))
                .peek(it -> it.setStatus(ExamStatus.FINISHED))
                .forEach(repository::save);
    }

}
