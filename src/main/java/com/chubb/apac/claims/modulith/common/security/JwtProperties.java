package com.chubb.apac.claims.modulith.common.security;
import jakarta.validation.constraints.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
@ConfigurationProperties(prefix="spring.security.jwt") @Validated
public record JwtProperties(@NotBlank @Size(min=32) String secret,@Positive long expiration) {}
