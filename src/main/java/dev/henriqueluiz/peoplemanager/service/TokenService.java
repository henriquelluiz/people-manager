package dev.henriqueluiz.peoplemanager.service;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.web.response.TokenResponse;
import org.springframework.security.core.Authentication;

public interface TokenService {
    TokenResponse getTokens(Authentication authentication);
    TokenResponse refreshTokens(String token);
}
