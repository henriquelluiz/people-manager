package dev.henriqueluiz.peoplemanager.service;
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
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
public class AddServiceTest {

    @Autowired
    AddService service;

    @Test
    @Sql(
            scripts = { "insertPerson.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteAll.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenPerson_whenCall_thenPersonEntityIsExpected() {
        Address address = new Address();
        address.setStreet("Av. Oscar");
        address.setPostalCode("24675942");
        address.setPreferred(false);
        address.setCity("São Paulo");
        address.setNumber("476A");

        Address result = service.saveAddress(1L, address);
        assertThat(result.getAddressId()).isNotNull();
        assertThat(result.getCity()).isEqualTo(address.getCity());
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
    void givenPersonAndAddressIds_whenCall_thenAddressEntityIsExpected() {
        Address entity = service.getAddressById(1L, 1L);
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
    void givenPersonAndAddressIds_whenCall_thenNoErrorShouldOccur() {
        assertThatCode(() -> service.setPreferentialAddress(1L, 1L))
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
    void givenPersonId_whenCall_thenPreferredAddressEntityIsExpected() {
        Address entity = service.getPreferentialAddress(1L);
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
    void givenPersonId_whenCall_thenPageWithTwoEntitiesIsExpected() {
        Pageable pageable = Pageable.ofSize(2).withPage(0);
        Page<Address> page = service.getAll(1L, pageable);
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isEqualTo(2);
    }
}
