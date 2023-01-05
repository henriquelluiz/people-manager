package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.web.response.TokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.Collections;

import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
public class TokenServiceTest {

    @Autowired
    TokenService tokenService;

    @Autowired
    JwtEncoder encoder;

    @Autowired
    JwtDecoder decoder;

    final String USERNAME = "test@mail.dev";
    final String PASSWORD = "developer";

    @Test
    @WithMockUser(value = USERNAME, password = PASSWORD)
    void givenAuthentication_whenCall_thenTokensAreExpected() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TokenResponse tokens = tokenService.getTokens(authentication);

        String accessToken = (String) tokens.getAccessToken().get("token");
        String subject1 = decoder.decode(accessToken).getSubject();

        String refreshToken = (String) tokens.getRefreshToken().get("token");
        String subject2 = decoder.decode(refreshToken).getSubject();

        assertThat(tokens).isNotNull();
        assertThat(tokens.getType()).isEqualTo("bearer");
        assertThat(subject1).isEqualTo(USERNAME);
        assertThat(subject2).isEqualTo(USERNAME);
        assertThat(tokens.getAccessToken().get("exp")).isNotNull();
        assertThat(tokens.getRefreshToken().get("exp")).isNotNull();
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
    void givenRefreshToken_whenCall_thenNewTokensAreExpected() {
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(5, MINUTES))
                .subject(USERNAME)
                .claim("scope", Collections.emptyList())
                .build();

        String refresh = encoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
        TokenResponse tokens = tokenService.refreshTokens(refresh);

        String accessToken = (String) tokens.getAccessToken().get("token");
        String subject1 = decoder.decode(accessToken).getSubject();

        String refreshToken = (String) tokens.getRefreshToken().get("token");
        String subject2 = decoder.decode(refreshToken).getSubject();

        assertThat(tokens).isNotNull();
        assertThat(tokens.getType()).isEqualTo("bearer");
        assertThat(subject1).isEqualTo(USERNAME);
        assertThat(subject2).isEqualTo(USERNAME);
        assertThat(tokens.getAccessToken().get("exp")).isNotNull();
        assertThat(tokens.getRefreshToken().get("exp")).isNotNull();
    }
}
