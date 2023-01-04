package dev.henriqueluiz.peoplemanager.repository;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    @Test
    @Sql(scripts = "insertUser.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "deleteUser.sql", executionPhase = AFTER_TEST_METHOD)
    void givenUserEmail_whenRun_thenUserEntityIsExpected() {
        AppUser user = repository.findByEmail("test@mail.dev").orElseThrow();
        assertThat(user.getUserId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("test@mail.dev");
    }
}
