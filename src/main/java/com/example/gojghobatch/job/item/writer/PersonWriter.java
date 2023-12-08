package com.example.gojghobatch.job.item.writer;

import com.example.gojghobatch.record.Person;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonWriter {

  private final DataSource dataSource;

  @Bean
  public JdbcBatchItemWriter<Person> writer() {
    return new JdbcBatchItemWriterBuilder<Person>()
        .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
        .itemPreparedStatementSetter((item, ps) -> {
          ps.setString(1, item.firstName());
          ps.setString(2, item.lastName());
          // Set other parameters if needed
        })
        .dataSource(dataSource)
        .beanMapped()
        .build();
  }
}
