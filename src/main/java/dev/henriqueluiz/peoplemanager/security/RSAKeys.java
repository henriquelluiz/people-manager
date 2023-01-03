package dev.henriqueluiz.peoplemanager.security;
/*
 * @Author: Henrique Luiz
 * @LinkedIn: heenluy
 * @Github: heenluy
 */

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Validated
@ConfigurationProperties(prefix = "rsa")
public record RSAKeys(@NotNull RSAPrivateKey privateKey, @NotNull RSAPublicKey publicKey) {}
