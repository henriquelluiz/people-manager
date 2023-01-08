package dev.henriqueluiz.peoplemanager.exception;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.henriqueluiz.peoplemanager.web.request.RoleRequest;
import dev.henriqueluiz.peoplemanager.web.request.RoleUserRequest;
import dev.henriqueluiz.peoplemanager.web.request.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RestExceptionHandlerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;
    final String JSON_HAL_VALUE = "application/hal+json";

    @Test
    void givenInvalidFields_whenThrow_thenBadRequestResponseIsExpected() throws Exception {
        UserRequest user = new UserRequest("invalid.dev", "");
        String request = mapper.writeValueAsString(user);

        ResultActions result = mvc.perform(
                post("/users/save")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .content(request));

        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.status").value(400));
        result.andExpect(jsonPath("$.title").value("Bad request"));
        result.andExpect(jsonPath("$.fields").isNotEmpty());
        result.andDo(print());
    }

    @Test
    void givenNonExistentRole_whenCall_thenNotFoundResponseStatusIsExpected() throws Exception {
        var requestBody = new RoleUserRequest("non-existent", "test@mail.dev");
        String request = mapper.writeValueAsString(requestBody);
        ResultActions result = mvc.perform(
                put("/roles/add")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .content(request));

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.status").value(404));
        result.andExpect(jsonPath("$.title").value("Not found"));
        result.andExpect(jsonPath("$.details").value("Role not found"));
        result.andDo(print());
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
    void givenNonExistentUserEmail_whenThrow_thenNotFoundResponseIsExpected() throws Exception {
        var requestBody = new RoleUserRequest("test", "non-existent@mail.dev");
        String request = mapper.writeValueAsString(requestBody);
        ResultActions result = mvc.perform(
                put("/roles/add")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .content(request));

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.status").value(404));
        result.andExpect(jsonPath("$.title").value("Not found"));
        result.andExpect(jsonPath("$.details").value("Username not found"));
        result.andDo(print());
    }

    @Test
    void givenInvalidUserRequest_whenThrow_thenBadCredentialsResponseIsExpected() throws Exception {
        String request = mapper.writeValueAsString(new UserRequest("invalid@mail.dev", "invalid"));
        ResultActions result = mvc.perform(
                post("/token")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .content(request));

        result.andExpect(status().isUnauthorized());
        result.andExpect(jsonPath("$.status").value(401));
        result.andExpect(jsonPath("$.title").value("Bad credentials"));
        result.andExpect(jsonPath("$.details").value("Username or password are not valid"));
        result.andDo(print());
    }

    @Test
    @Sql(
            scripts = { "insertRole.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @WithMockUser(authorities = { "SCOPE_manager" })
    void givenExistentRole_whenThrow_thenBadRequestResponseIsExpected() throws Exception {
        var role = new RoleRequest("test");
        String request = mapper.writeValueAsString(role);

        ResultActions result = mvc.perform(
                post("/roles/save")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .content(request));

        result.andExpect(status().isBadRequest());
        result.andExpect(jsonPath("$.status").value(400));
        result.andExpect(jsonPath("$.title").value("Bad request"));
        result.andExpect(jsonPath("$.details").value("Email or role name already exists"));
        result.andDo(print());
    }

    @Test
    void givenManagerOrAdminRoleRequest_whenThrow_thenMethodNotAllowedResponseIsExpected() throws Exception {
        var requestBody = new RoleUserRequest("manager", "test@mail.dev");
        String request = mapper.writeValueAsString(requestBody);
        ResultActions result = mvc.perform(
                put("/roles/add")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .content(request));

        result.andExpect(status().isMethodNotAllowed());
        result.andExpect(jsonPath("$.status").value(405));
        result.andExpect(jsonPath("$.title").value("Role not Allowed"));
        result.andExpect(jsonPath("$.details").value("Only managers can add manager or admin roles"));
        result.andDo(print());
    }

    @Test
    void givenNoNExistentPersonId_whenThrow_thenNotFoundResponseIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                get("/persons/get")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .param("personId", String.valueOf(1))
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_read"))));

        result.andExpect(status().isNotFound());
        result.andExpect(jsonPath("$.status").value(404));
        result.andExpect(jsonPath("$.title").value("Not found"));
        result.andExpect(jsonPath("$.details").value("Entity not found"));
        result.andDo(print());
    }
}
