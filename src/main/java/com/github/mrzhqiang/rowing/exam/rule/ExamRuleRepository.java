package com.github.mrzhqiang.rowing.exam.rule;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "exam-rule", excerptProjection = ExamRuleExcerpt.class)
public interface ExamRuleRepository extends BaseRepository<ExamRule> {

    @RestResource(path = "list", rel = "list")
    List<ExamRule> findAllByTitleNotNull();

}
