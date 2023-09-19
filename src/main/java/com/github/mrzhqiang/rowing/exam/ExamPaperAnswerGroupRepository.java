package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "exam-paper-answer-group", excerptProjection = ExamPaperAnswerGroupExcerpt.class)
public interface ExamPaperAnswerGroupRepository extends BaseRepository<ExamPaperAnswerGroup> {

}
