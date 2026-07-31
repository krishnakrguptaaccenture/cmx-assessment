package com.chubb.apac.claims.modulith.user.dto.request;
import jakarta.validation.constraints.*;
public record RegisterClaimantRequest(@NotBlank @Email @Size(max=254) String email,
 @NotBlank @Size(min=8,max=128) String password,
 @NotBlank @Size(max=150) String fullName,
 @Size(max=30) String phoneNumber) {}
