package com.example.gojghobatch.job.listener;

import com.example.gojghobatch.record.Person;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobCompleteNotificationListener implements JobExecutionListener {

  private final JdbcTemplate jdbcTemplate;

  @Override
  public void beforeJob(JobExecution jobExecution) {
    //nothing to do
  }

  /**
   * Callback after completion of a job. Called after both successful and failed
   * executions. To perform logic on a particular status, use
   * {@code if (jobExecution.getStatus() == BatchStatus.X)}.
   * @param jobExecution the current {@link JobExecution}
   */

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      log.info("JOB FINISHED ~ !");
    }

    jdbcTemplate.query("SELECT first_name, last_name FROM people", new DataClassRowMapper<>(Person.class))
        .forEach(person -> log.info("Found <{{}}> in the database.", person));

  }

}
