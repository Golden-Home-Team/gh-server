package kr.co.goldenhome;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job [{}] started at: {}", jobExecution.getJobInstance().getJobName(), jobExecution.getStartTime());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        LocalDateTime startTime = jobExecution.getStartTime();
        LocalDateTime endTime = jobExecution.getEndTime();

        if (startTime != null && endTime != null) {
            Duration duration = Duration.between(startTime, endTime);
            long seconds = duration.getSeconds();
            long millis = duration.toMillis() % 1000;
            log.info("Job [{}] completed. Status: {}. Total time taken: {} seconds {} milliseconds",
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus(),
                    seconds,
                    millis);
        } else {
            log.warn("Job [{}] completed, but start or end time is missing. Status: {}",
                    jobExecution.getJobInstance().getJobName(),
                    jobExecution.getStatus());
        }

        if (jobExecution.getStatus().isUnsuccessful()) {
            jobExecution.getAllFailureExceptions().forEach(e ->
                    log.error("Job [{}] failed with exception: {}", jobExecution.getJobInstance().getJobName(), e.getMessage(), e)
            );
        }
    }
}
