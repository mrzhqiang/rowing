package com.github.mrzhqiang.rowing.exam.paper;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "exam-paper-answer-card", excerptProjection = ExamPaperAnswerCardExcerpt.class)
public interface ExamPaperAnswerCardRepository extends BaseRepository<ExamPaperAnswerCard> {

}
