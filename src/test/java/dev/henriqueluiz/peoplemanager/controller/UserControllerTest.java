package dev.henriqueluiz.peoplemanager.controller;
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
public class UserControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;
    final String JSON_HAL_VALUE = "application/hal+json";

    @Test
    @Sql(
            scripts = { "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenUserRequest_whenCall_thenCreatedResponseStatusIsExpected() throws Exception {
        UserRequest user = new UserRequest("test@mail.dev", "developer");
        String request = mapper.writeValueAsString(user);

        ResultActions result = mvc.perform(
                post("/users/save")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .content(request));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.email").value(user.email()));
        result.andExpect(jsonPath("$.authorities").isEmpty());
        result.andDo(print());
    }

    @Test
    @Sql(
            scripts = { "deleteRole.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    @WithMockUser(authorities = { "SCOPE_manager" })
    void givenRoleObject_whenCall_thenCreatedResponseStatusIsExpected() throws Exception {
        var role = new RoleRequest("test");
        String request = mapper.writeValueAsString(role);

        ResultActions result = mvc.perform(
                post("/roles/save")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .content(request));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.name").value(role.roleName()));
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
    void givenPageSize_whenCall_thenPageOfRolesNotEmptyIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                get("/roles/get/all")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(3))
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$._embedded.roleList").isNotEmpty());
        result.andExpect(jsonPath("$._embedded.roleList[0].name").value("test"));
        result.andDo(print());
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
    void givenUserEmail_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        String email = "test@mail.dev";
        ResultActions result = mvc.perform(
                get("/users/get")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .param("email", email)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_manager"))));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.authorities").isArray());
        result.andExpect(jsonPath("$.email").value(email));
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
    void givenUserEmail_whenCall_thenNoContentResponseStatusIsExpected() throws Exception {
        String email = "test@mail.dev";
        ResultActions result = mvc.perform(
                delete("/users/delete")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .param("email", email)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_manager"))));

        result.andExpect(status().isNoContent());
    }

    @Test
    @Sql(
            scripts = { "insertRole.sql", "insertUser.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteRole.sql", "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenRoleNameAndUserEmail_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        var requestBody = new RoleUserRequest("test", "test@mail.dev");
        String request = mapper.writeValueAsString(requestBody);
        ResultActions result = mvc.perform(
                put("/roles/add")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .content(request));

        result.andExpect(status().isOk());
    }

}
