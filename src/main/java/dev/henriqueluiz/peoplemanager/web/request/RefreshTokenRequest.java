package dev.henriqueluiz.peoplemanager.web.request;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String token) {}
