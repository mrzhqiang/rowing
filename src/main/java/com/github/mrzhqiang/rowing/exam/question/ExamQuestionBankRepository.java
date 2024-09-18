package com.github.mrzhqiang.rowing.exam.question;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasRole('USER')")
@RepositoryRestResource(path = "exam-question-bank", excerptProjection = ExamQuestionBankExcerpt.class)
public interface ExamQuestionBankRepository extends BaseRepository<ExamQuestionBank> {

    @RestResource(path = "page", rel = "page")
    Page<ExamQuestionBank> findAllByTitleContainingAndSubject_ValueIn(String title,
                                                                      List<String> subjects,
                                                                      Pageable pageable);

    @RestResource(path = "list", rel = "list")
    List<ExamQuestionBank> findAllByTitleNotNullOrderByCreatedDesc();

}
