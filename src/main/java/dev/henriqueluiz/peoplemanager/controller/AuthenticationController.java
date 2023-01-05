package dev.henriqueluiz.peoplemanager.controller;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import dev.henriqueluiz.peoplemanager.service.TokenService;
import dev.henriqueluiz.peoplemanager.web.request.RefreshTokenRequest;
import dev.henriqueluiz.peoplemanager.web.request.UserRequest;
import dev.henriqueluiz.peoplemanager.web.response.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping(value = { "/token" }, produces = { "application/json" })
    public ResponseEntity<TokenResponse> generateTokens(@RequestBody @Valid UserRequest req) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(req.email(), req.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        return ResponseEntity.ok(tokenService.getTokens(authentication));
    }

    @GetMapping(value = { "/refresh" }, produces = { "application/json" })
    public ResponseEntity<TokenResponse> refreshTokens(@RequestBody @Valid RefreshTokenRequest req) {
        return ResponseEntity.ok(tokenService.refreshTokens(req.token()));
    }
}
