package com.github.mrzhqiang.rowing.exam.rule;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasRole('USER')")
@RepositoryRestResource(path = "exam-rule", excerptProjection = ExamRuleExcerpt.class)
public interface ExamRuleRepository extends BaseRepository<ExamRule> {

    @RestResource(path = "list", rel = "list")
    List<ExamRule> findAllByTitleNotNull();

}
