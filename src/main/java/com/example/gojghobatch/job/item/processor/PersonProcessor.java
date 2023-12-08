package com.example.gojghobatch.job.item.processor;

import com.example.gojghobatch.record.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PersonProcessor implements ItemProcessor<Person, Person> {

  @Override
  public Person process(Person item) throws Exception {
    final String firstName = item.firstName().toUpperCase();
    final String lastName = item.lastName().toUpperCase();

    final Person transformedPerson = new Person(firstName, lastName);

    log.info("Converting (" + item + ") into (" + transformedPerson + ")");
    return transformedPerson;
  }
}
