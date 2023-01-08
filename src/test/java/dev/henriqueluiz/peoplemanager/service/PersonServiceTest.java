package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
public class PersonServiceTest {

    @Autowired
    PersonService service;

    @Test
    @Sql(
            scripts = { "deleteAll.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenPerson_whenCall_thenPersonEntityIsExpected() {
        Person person = new Person("Henrique", "Luiz", LocalDate.of(1999, 6, 14));
        Person result = service.savePerson(person);
        assertThat(result.getPersonId()).isNotNull();
        assertThat(result.getDateOfBirth()).isEqualTo(person.getDateOfBirth());
    }

    @Test
    @Sql(
            scripts = { "insertAll.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteAll.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenIdAndPerson_whenCall_thenPersonEntityIsExpected() {
        Person person = new Person("Joseph", "Carlos", LocalDate.of(1998, 9, 21));
        assertThatCode(() -> service.updatePerson(1L, person))
                .doesNotThrowAnyException();
    }

    @Test
    @Sql(
            scripts = { "insertAll.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteAll.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenId_whenCall_thenNoErrorShouldOccur() {
        assertThatCode(() -> service.deletePerson(1L))
                .doesNotThrowAnyException();
    }

    @Test
    @Sql(
            scripts = { "insertAll.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteAll.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenId_whenCall_thenPersonEntityIsExpected() {
        Person result = service.getPersonById(1L);
        assertThat(result.getPersonId()).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Henrique");
    }

    @Test
    @Sql(
            scripts = { "insertAll.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteAll.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenPageSize_whenCall_thenPageOfPersonNotEmptyIsExpected() {
        Pageable pageable = Pageable.ofSize(1).withPage(0);
        Page<Person> page = service.getAll(pageable);
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isGreaterThan(0);
    }
}
