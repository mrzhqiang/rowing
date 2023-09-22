package com.github.mrzhqiang.rowing.exam;

import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Nonnull;

/**
 * 考试相关任务。
 * <p>
 */
public class ExamJob extends QuartzJobBean {

    private ExamService examService;

    public void setExamService(ExamService examService) {
        this.examService = examService;
    }

    @Override
    protected void executeInternal(@Nonnull JobExecutionContext context) {
        examService.updateStatus();
    }

}
