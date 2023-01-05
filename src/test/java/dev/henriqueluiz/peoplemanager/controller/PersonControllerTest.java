package dev.henriqueluiz.peoplemanager.controller;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.henriqueluiz.peoplemanager.model.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

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
public class PersonControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;
    final String JSON_HAL_VALUE = "application/hal+json";

    @Test
    @Sql(
            scripts = { "deleteAll.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenPerson_whenCall_thenCreatedResponseStatusIsExpected() throws Exception {
        Person person = new Person(null, "Joseph", "Carlos", LocalDate.of(1998, 9, 21));
        String request = mapper.writeValueAsString(person);

        ResultActions result = mvc.perform(
                post("/persons/save")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .content(request)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_write"))));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.firstName").value(person.getFirstName()));
        result.andExpect(jsonPath("$.lastName").value(person.getLastName()));
        result.andDo(print());
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
    void givenIdAndPerson_whenCall_thenNoContentResponseStatusIsExpected() throws Exception {
        Person person = new Person(null, "Joseph", "Carlos", LocalDate.of(1998, 9, 21));
        String request = mapper.writeValueAsString(person);

        ResultActions result = mvc.perform(
                put("/persons/update")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .param("personId", String.valueOf(1))
                        .content(request)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_write"))));
        result.andExpect(status().isNoContent());
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
    void givenPersonId_whenCall_thenNoContentResponseStatusIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                delete("/persons/delete")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .param("personId", String.valueOf(1))
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_write"))));
        result.andExpect(status().isNoContent());
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
    void givenPersonId_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                get("/persons/get")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .param("personId", String.valueOf(1))
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_read"))));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.firstName").value("Henrique"));
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
    void givenPageSize_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                get("/persons/get/all")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(1))
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_read"))));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$._embedded.personList[0].firstName").value("Henrique"));
    }
}
