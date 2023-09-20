package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import com.github.mrzhqiang.rowing.domain.ExamQuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "exam-question", excerptProjection = ExamQuestionExcerpt.class)
public interface ExamQuestionRepository extends BaseRepository<ExamQuestion> {

    @RestResource(path = "page", rel = "page")
    Page<ExamQuestion> findAllByCodeContainingAndTypeInAndStemContaining(String code,
                                                                         List<ExamQuestionType> types,
                                                                         String stem,
                                                                         Pageable pageable);

    @RestResource(path = "list", rel = "list")
    List<ExamQuestion> findAllByType(ExamQuestionType type);

}
