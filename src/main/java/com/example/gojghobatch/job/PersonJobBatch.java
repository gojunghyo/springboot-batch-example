package com.example.gojghobatch.job;

import com.example.gojghobatch.job.item.processor.PersonProcessor;
import com.example.gojghobatch.job.item.reader.PersonReader;
import com.example.gojghobatch.job.item.writer.PersonWriter;
import com.example.gojghobatch.job.listener.JobCompleteNotificationListener;
import com.example.gojghobatch.record.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ConditionalOnProperty(name = "spring.batch.job.names", havingValue = PersonJobBatch.JOB_NAME) //무분별한 빈생성 방지 arguments로 넘어온 조건이 맞을시 생성됨 (JOB)
@RequiredArgsConstructor
public class PersonJobBatch {

  static final String JOB_NAME = "project.personJob";

  private final String STEP_NAME = "project.personStep";
  private static final int CHUNK_SIZE = 10;
  private final JobCompleteNotificationListener jobCompleteNotificationListener;
  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;
  private final PersonReader personReader;
  private final PersonProcessor personProcessor;
  private final PersonWriter personWriter;


  @Bean(name = JOB_NAME)
  public Job personJob() {
    return new JobBuilder(JOB_NAME, jobRepository)
        .listener(jobCompleteNotificationListener)
        .start(this.personStep())
        .preventRestart()
        .build();
  }

  @Bean(name = STEP_NAME)
  public Step personStep() {
    return new StepBuilder(STEP_NAME, jobRepository)
        .<Person, Person>chunk(CHUNK_SIZE)
        .transactionManager(platformTransactionManager)
        .reader(personReader.reader())
        .processor(personProcessor)
        .writer(personWriter.writer())
        .build();
  }

}
