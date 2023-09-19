package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.annotation.Nonnull;
import java.util.List;

@RepositoryRestResource(path = "exam-rule", excerptProjection = ExamRuleExcerpt.class)
public interface ExamRuleRepository extends BaseRepository<ExamRule> {

    @RestResource(path = "list", rel = "list")
    @Nonnull
    List<ExamRule> findAll();

}
