package dev.henriqueluiz.peoplemanager.controller;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.henriqueluiz.peoplemanager.web.request.RefreshTokenRequest;
import dev.henriqueluiz.peoplemanager.web.request.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    JwtEncoder encoder;

    final String USERNAME = "test@mail.dev";
    final String PASSWORD = "test";

    @Test
    @Sql(
            scripts = { "insertUser.sql" },
            executionPhase = BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = { "deleteUser.sql" },
            executionPhase = AFTER_TEST_METHOD
    )
    void givenUserRequest_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        String request = mapper.writeValueAsString(new UserRequest(USERNAME, PASSWORD));
        ResultActions result = mvc.perform(
                post("/token")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .with(csrf())
                        .content(request));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.type").value("bearer"));
        result.andExpect(jsonPath("$.access_token").exists());
        result.andExpect(jsonPath("$.refresh_token").exists());
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
    void givenRefreshToken_whenCall_thenOkResponseStatusIsExpected() throws Exception {
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject(USERNAME)
                .claim("scope", Collections.emptyList())
                .build();

        String refreshToken = encoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
        String request = mapper.writeValueAsString(new RefreshTokenRequest(refreshToken));

        ResultActions result = mvc.perform(
                post("/refresh")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON)
                        .with(csrf())
                        .content(request));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.type").value("bearer"));
        result.andExpect(jsonPath("$.access_token").exists());
        result.andExpect(jsonPath("$.refresh_token").exists());
        result.andDo(print());
    }
}
