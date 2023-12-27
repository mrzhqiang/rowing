package com.github.mrzhqiang.rowing.config;

import com.github.mrzhqiang.rowing.exam.ExamJob;
import com.github.mrzhqiang.rowing.exam.ExamService;
import com.google.common.collect.ImmutableMap;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 调度任务配置。
 * <p>
 */
@EnableScheduling
@Configuration
public class SchedulingConfiguration {

    @Bean
    public JobDetail examJob(ExamService service) {
        return JobBuilder.newJob(ExamJob.class)
                .withIdentity("exam-job", "exams")
                .usingJobData(new JobDataMap(ImmutableMap.of("examService", service)))
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger examTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob("exam-job", "exams")
                .withIdentity("exam-trigger", "exams")
                // 每分钟反复执行
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(1).repeatForever())
                .build();
    }

}
