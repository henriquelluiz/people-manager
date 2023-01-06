package dev.henriqueluiz.peoplemanager.repository;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    RoleRepository repository;

    @Test
    @Sql(scripts = "insertRole.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "deleteRole.sql", executionPhase = AFTER_TEST_METHOD)
    void givenRoleName_whenRun_thenRoleEntityIsExpected() {
        Role role = repository.findByName("test_0").orElseThrow();
        assertThat(role.getId()).isNotNull();
        assertThat(role.getName()).isEqualTo("test_0");
    }
}
