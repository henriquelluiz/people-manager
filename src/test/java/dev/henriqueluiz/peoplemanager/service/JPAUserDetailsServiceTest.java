package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.service.detail.JPAUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
class JPAUserDetailsServiceTest {

    @Autowired
    JPAUserDetailsService userDetailsService;
    @Test
    @Sql(
            scripts = { "insertUser.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenUserEmail_whenCall_thenUserDetailsIsExpected() {
        assertThatCode(() -> userDetailsService.loadUserByUsername("test@mail.dev"))
                .doesNotThrowAnyException();

        assertThat(userDetailsService.loadUserByUsername("test@mail.dev"))
                .isInstanceOf(UserDetails.class);
    }
}
