package com.github.mrzhqiang.rowing.exam.paper;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.exam.Exam;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@PreAuthorize("hasRole('USER')")
@RepositoryRestResource(path = "exam-paper", excerptProjection = ExamPaperExcerpt.class)
public interface ExamPaperRepository extends BaseRepository<ExamPaper> {

    @RestResource(exported = false)
    Optional<ExamPaperInfo> findByTaker_UsernameAndExam(String username, Exam exam);

    @RestResource(exported = false)
    Optional<ExamPaperInfo> findByMarker_UsernameAndExam(String username, Exam exam);

}
