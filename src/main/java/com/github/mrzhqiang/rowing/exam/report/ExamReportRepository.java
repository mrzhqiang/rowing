package com.github.mrzhqiang.rowing.exam.report;

import com.github.mrzhqiang.rowing.domain.BaseRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('USER')")
@RepositoryRestResource(path = "exam-report", excerptProjection = ExamReportExcerpt.class)
public interface ExamReportRepository extends BaseRepository<ExamReport> {

}
