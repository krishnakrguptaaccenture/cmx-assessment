package com.chubb.apac.claims.modulith.user.dto.request;
import jakarta.validation.constraints.*;
public record LoginRequest(@NotBlank @Email String email,@NotBlank String password) {}
