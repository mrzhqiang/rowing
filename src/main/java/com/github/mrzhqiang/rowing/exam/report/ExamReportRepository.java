package com.github.mrzhqiang.rowing.exam.report;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "exam-report", excerptProjection = ExamReportExcerpt.class)
public interface ExamReportRepository extends BaseRepository<ExamReport> {

}
