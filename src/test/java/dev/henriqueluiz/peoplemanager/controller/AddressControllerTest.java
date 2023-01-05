package dev.henriqueluiz.peoplemanager.controller;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.henriqueluiz.peoplemanager.model.Address;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
public class AddressControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;
    final String JSON_HAL_VALUE = "application/hal+json";

    @Test
    @Sql(
            scripts = { "insertPerson.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteAll.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenAddress_whenCall_thenCreatedResponseStatusIsExpected() throws Exception {
        Address address = new Address();
        address.setStreet("Av. Oscar");
        address.setPostalCode("24675942");
        address.setPreferred(false);
        address.setCity("São Paulo");
        address.setNumber("476A");
        String request = mapper.writeValueAsString(address);

        ResultActions result = mvc.perform(
                post("/addresses/save")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .param("personId", String.valueOf(1))
                        .content(request)
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_write"))));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.street").value(address.getStreet()));
        result.andExpect(jsonPath("$.city").value(address.getCity()));
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
    void givenPersonAndAddressIds_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                get("/addresses/get")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .param("personId", String.valueOf(1))
                        .param("addressId", String.valueOf(1))
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_write"))));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.street").value("Av. Universal"));
        result.andExpect(jsonPath("$.city").value("Salvador"));
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
    void givenPersonId_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                get("/addresses/get/preferred")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .param("personId", String.valueOf(1))
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_write"))));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.street").value("Av. Hebert"));
        result.andExpect(jsonPath("$.city").value("Rio de Janeiro"));
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
    void givenPersonAndAddressIds_whenCall_thenNoContentResponseStatusIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                put("/addresses/edit/preferred")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .param("personId", String.valueOf(1))
                        .param("addressId", String.valueOf(1))
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
    void givenPageSize_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        ResultActions result = mvc.perform(
                get("/addresses/get/all")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(JSON_HAL_VALUE)
                        .param("personId", String.valueOf(1))
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(2))
                        .with(jwt().authorities(new SimpleGrantedAuthority("SCOPE_write"))));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$._embedded.addressList").isNotEmpty());
        result.andExpect(jsonPath("$._embedded.addressList[0].city").value("Salvador"));
        result.andDo(print());
    }
}
