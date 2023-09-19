package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "exam-paper-answer", excerptProjection = ExamPaperAnswerExcerpt.class)
public interface ExamPaperAnswerRepository extends BaseRepository<ExamPaperAnswer> {

}
