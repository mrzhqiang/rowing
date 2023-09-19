package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "exam-question-option", excerptProjection = ExamQuestionOptionExcerpt.class)
public interface ExamQuestionOptionRepository extends BaseRepository<ExamQuestionOption> {

}
