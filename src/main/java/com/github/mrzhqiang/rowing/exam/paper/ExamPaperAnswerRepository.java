package com.github.mrzhqiang.rowing.exam.paper;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('USER')")
@RepositoryRestResource(path = "exam-paper-answer", excerptProjection = ExamPaperAnswerExcerpt.class)
public interface ExamPaperAnswerRepository extends BaseRepository<ExamPaperAnswer> {

}
