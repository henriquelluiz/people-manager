package dev.henriqueluiz.peoplemanager.repository;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
public class AddressRepositoryTest {

    @Autowired
    AddressRepository repository;

    @Test
    @Sql(
            scripts = { "insertAll.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteAll.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenPersonAndAddressIds_whenRun_thenAddressEntityIsExpected() {
        Address entity = repository.findByPersonId(1L, 1L).orElseThrow();
        assertThat(entity.getAddressId()).isNotNull();
        assertThat(entity.getCity()).isEqualTo("Salvador");
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
    void givenPersonId_whenRun_thenPreferredAddressEntityIsExpected() {
        Address entity = repository.findByPreferred(1L).orElseThrow();
        assertThat(entity.getAddressId()).isNotNull();
        assertThat(entity.getCity()).isEqualTo("Rio de Janeiro");
        assertThat(entity.getPreferred()).isEqualTo(true);
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
    void givenPersonId_whenRun_thenPageWithTwoEntitiesIsExpected() {
        Pageable pageable = Pageable.ofSize(2).withPage(0);
        Page<Address> page = repository.findAllByPersonId(1L, pageable);
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(2);
    }

}
