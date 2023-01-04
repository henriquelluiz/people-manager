package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.exception.model.InvalidTokenException;
import dev.henriqueluiz.peoplemanager.model.AppUser;
import dev.henriqueluiz.peoplemanager.repository.UserRepository;
import dev.henriqueluiz.peoplemanager.web.response.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.TemporalUnit;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.MINUTES;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserRepository userRepository;

    @Override
    public TokenResponse getTokens(Authentication authentication) {
        log.debug("Generating token for user: '{}'", authentication.getName());
        Instant now = Instant.now();
        List<String> scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        String accessToken = getEncodedToken(authentication.getName(), scope, 25L, MINUTES);
        String refreshToken = getEncodedToken(authentication.getName(), new ArrayList<>(), 1L, DAYS);
        return buildTokenResponse(
                accessToken,
                refreshToken,
                now.plus(25L, MINUTES),
                now.plus(1L, DAYS)
        );
    }

    @Override
    public TokenResponse refreshTokens(String token) {
        AppUser user = getUserBySubject(token);
        log.debug("Refreshing token for user: '{}'", user.getEmail());

        Instant now = Instant.now();
        List<String> scope = user.getAuthorities().stream().toList();
        String accessToken = getEncodedToken(user.getEmail(), scope, 25L, MINUTES);
        String refreshToken = getEncodedToken(user.getEmail(), new ArrayList<>(), 1L, DAYS);
        return buildTokenResponse(
                accessToken,
                refreshToken,
                now.plus(25L, MINUTES),
                now.plus(1L, DAYS)
        );
    }

    private String getEncodedToken(String sub, List<String> scope, Long exp, TemporalUnit unit) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(exp, unit))
                .subject(sub)
                .claim("scope", scope)
                .build();

        return this.jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    private AppUser getUserBySubject(String token) {
        Jwt jwt = this.jwtDecoder.decode(token);
        String email = Objects.requireNonNull(jwt.getSubject());
        Instant expiresAt = Objects.requireNonNull(jwt.getExpiresAt());

        if((expiresAt.isBefore(Instant.now()))) {
            throw new InvalidTokenException("Token is not valid");
        }

        return userRepository.findByEmail(email).orElseThrow(() -> {
            log.debug("User not found: '{}'", email);
            return new UsernameNotFoundException(String.format("User not found: '%s'", email));
        });
    }

    private TokenResponse buildTokenResponse(String access, String refresh, Instant accessExp, Instant refreshExp) {
        Map<String, Object> accessToken = new HashMap<>(2);
        accessToken.put("token", access);
        accessToken.put("exp", accessExp);

        Map<String, Object> refreshToken = new HashMap<>(2);
        refreshToken.put("token", refresh);
        refreshToken.put("exp", refreshExp);
        return new TokenResponse("bearer", accessToken, refreshToken);
    }
}
