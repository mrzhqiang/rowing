package com.github.mrzhqiang.rowing.exam;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "exam-paper", excerptProjection = ExamPaperExcerpt.class)
public interface ExamPaperRepository extends BaseRepository<ExamPaper> {

}
