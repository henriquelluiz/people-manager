package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */


import dev.henriqueluiz.peoplemanager.model.AppUser;
import dev.henriqueluiz.peoplemanager.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService service;

    @Test
    @Sql(
            scripts = { "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenUser_whenCall_thenUserUserEntityIsExpected() {
        AppUser user = new AppUser();
        user.setEmail("test@mail.dev");
        user.setPassword("test");
        user.setAuthorities(Collections.emptyList());

        AppUser result = service.saveUser(user);
        assertThat(result.getUserId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    @Sql(
            scripts = { "insertUser.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenUserEmail_whenCall_thenUserEntityIsExpected() {
        String email = "test@mail.dev";
        AppUser user = service.getUser(email);
        assertThat(user.getUserId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @Sql(
            scripts = { "insertUser.sql", "insertRole.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteUser.sql", "deleteRole.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenUserEmail_whenCall_thenNoErrorShouldOccur() {
        assertThatCode(() -> service.deleteUser("test@mail.dev"))
                .doesNotThrowAnyException();
    }

    @Test
    @Sql(
            scripts = { "deleteRole.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenRole_whenCall_thenRoleEntityIsExpected() {
        String roleName = "test";
        Role role = new Role(null, roleName);

        Role result = service.saveRole(role);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(roleName);
    }

    @Test
    @Sql(
            scripts = { "insertUser.sql", "insertRole.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteUser.sql", "deleteRole.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenRoleNameAndUserEmail_whenCall_thenNoErrorShouldOccur() {
        assertThatCode(() -> service.addRolesToUser("test", "test@mail.dev"))
                .doesNotThrowAnyException();
    }


    @Test
    @Sql(
            scripts = { "insertRole.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteRole.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenPageSize_whenCall_thenPageOfRolesNotEmptyIsExpected() {
        Pageable pageable = Pageable.ofSize(1).withPage(0);
        Page<Role> page = service.getRoles(pageable);
        assertThat(page).isNotNull();
        assertThat(page.getTotalElements()).isGreaterThan(0);
    }
}
