package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.account.Account;
import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.time.LocalDateTime;

@PreAuthorize("hasRole('USER')")
@RepositoryRestResource(path = "exam", excerptProjection = ExamExcerpt.class)
public interface ExamRepository extends BaseRepository<Exam> {

    @RestResource(path = "page", rel = "page")
    Page<Exam> findAllByTitleContainingAndCodeContainingAndStartTimeBetween(
            String title, String code,
            LocalDateTime firstStart, LocalDateTime secondStart,
            Pageable pageable);

    @RestResource(exported = false)
    Page<ExamInfo> findAllByTakersContainingAndTitleContainingAndCodeContainingAndStartTimeBetween(
            Account account, String title, String code,
            LocalDateTime firstStart, LocalDateTime secondStart,
            Pageable pageable);

    @RestResource(exported = false)
    Page<ExamInfo> findAllByMarkersContainingAndTitleContainingAndCodeContainingAndStartTimeBetween(
            Account account, String title, String code,
            LocalDateTime firstStart, LocalDateTime secondStart,
            Pageable pageable);

    @RestResource(exported = false)
    boolean existsByTakersContainingAndId(Account taker, Long examId);

    @RestResource(exported = false)
    boolean existsByMarkersContainingAndId(Account marker, Long examId);

}
