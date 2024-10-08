package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('USER')")
@RepositoryRestResource(path = "exam-question-option", excerptProjection = ExamQuestionOptionExcerpt.class)
public interface ExamQuestionOptionRepository extends BaseRepository<ExamQuestionOption> {

}
