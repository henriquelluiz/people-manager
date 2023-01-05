package dev.henriqueluiz.peoplemanager.exception;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.exception.model.RoleNotAllowedException;
import dev.henriqueluiz.peoplemanager.exception.model.RoleNotFoundException;
import dev.henriqueluiz.peoplemanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
public class UserExceptionTest {

    @Autowired
    UserService service;

    @Test
    void givenInvalidRoleName_whenThrown_thenRoleNotFoundExceptionIsExpected() {
        assertThatExceptionOfType(RoleNotFoundException.class)
                .isThrownBy(() -> service.addRolesToUser("dummy", "test@mail.dev"))
                .withMessage("Role not found: 'dummy'");
    }

    @Test
    void givenManagerOrAdminRoleName_whenThrown_thenRoleNotAllowedExceptionIsExpected() {
        assertThatExceptionOfType(RoleNotAllowedException.class)
                .isThrownBy(() -> service.addRolesToUser("manager", "test@mail.dev"))
                .withMessage("Role not allowed: 'manager'");
    }

    @Test
    void givenInvalidUserEmail_whenThrown_thenUsernameNotFoundExceptionIsExpected() {
        assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> service.getUser("test@not.found"))
                .withMessage("Username not found: 'test@not.found'");
    }
}
