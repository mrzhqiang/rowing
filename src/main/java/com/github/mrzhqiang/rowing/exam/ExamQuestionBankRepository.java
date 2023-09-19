package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "exam-question-bank", excerptProjection = ExamQuestionBankExcerpt.class)
public interface ExamQuestionBankRepository extends BaseRepository<ExamQuestionBank> {

    @RestResource(path = "page", rel = "page")
    Page<ExamQuestionBank> findAllByTitleContainingAndSubject_ValueIn(String title,
                                                                      List<String> subjects,
                                                                      Pageable pageable);

    @RestResource(path = "list", rel = "list")
    List<ExamQuestionBank> findAllByTitleNotNullOrderByCreatedDesc();

}
