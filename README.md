# springboot-batch-example

## Batch Job 만들기

`HelloBatch` 를 참조하여 Batch Job Configuration 을 구성한다.

* `@ConditionalOnProperty` 를 이용해 `spring.batch.job.names` 가 포함 돼 있을 경우에만 설정이 작동하게 하여 불필요한 Bean 생성을 막는다.
* Job 이름은 `project.의도한이름` 형태로 만든다.
* 인텔리제이에서 배치 어플리케이션 실행시 Program Arguments 부분에 아래 명령어를 넣는다
* --spring.batch.job.names=project.personJob (ConditionalOnProperty 에 맞게)

```java
import java.beans.BeanProperty;

@ConditionalOnProperty(name = "spring.batch.job.names", havingValue = HelloBatch.JOB_NAME)
@Configuration
@RequiredArgsConstructor
public class HelloBatch {
  
  static final String JOB_NAME = "project.helloJob";
  private static final String STEP_NAME = "project.helloStep";
  private static final int CHUNK_SIZE = 10; 
  private final JobBuilderFactory jobBuilderFactory;
  private final StepBuilderFactory stepBuilderFactory;
  private final PlatformTransactionManager projectTransactionManager;
  
  private final DataReader dataReader;
  private final DataProcessor dataProcessor;
  private final DataWriter dataWriter;

  @Bean(name = JOB_NAME)
  public Job helloJob() {
    return JobBuilderFactory.get(JOB_NAME)
        .incrementer(new ParamCleanRunIdIncrementer())
        .start(this.helloStep())
        .preventRestart() //배치 에러시 재실행
        .build();
  }
  
  @Bean(name = STEP_NAME)
  public Step helloStep() {
    return stepBuilderFactory.get(STEP_NAME)
        .transactionManager(projectTransactionManager)
        .<ProjectDto, ProjectDto> chunk(CHUNK_SIZE)
        .reader(dataReader)
        .processor(dataProcessor)
        .writer(dataWriter)
        .build();
  }
}
```


## Spring Batch meta-data table

스프링 배치 실행 JOB/STEP/결과 등 배치 어플리케이션을 운영하기 위한 메타데이터 테이블.
스프링 배치 DB가 H2인 경우 자동으로 테이블이 생성되며, 그 외 DB는 수동으로 테이블을 만들어줘야 한다.
```create database spring_batch``` 로 database를 생성한다.
DDL문은 spring-batch-core 라이브러리 안에 있다. (```schema-``` 로 파일 검색)
메타 데이터 테이블이 없으면 스프링 배치가 실행되지 않는다.
