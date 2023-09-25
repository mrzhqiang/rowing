package com.github.mrzhqiang.rowing.exam.mode;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "exam-mode", excerptProjection = ExamModeExcerpt.class)
public interface ExamModeRepository extends BaseRepository<ExamMode> {

}
