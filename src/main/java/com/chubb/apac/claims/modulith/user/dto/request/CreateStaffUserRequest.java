package com.chubb.apac.claims.modulith.user.dto.request;
import com.chubb.apac.claims.modulith.user.model.*;
import jakarta.validation.constraints.*;
import java.util.Set;
public record CreateStaffUserRequest(@NotBlank @Email String email,
 @NotBlank @Size(min=8,max=128) String password,
 @NotBlank @Size(max=150) String fullName,@Size(max=30) String phoneNumber,
 @NotNull UserRole role,@NotEmpty Set<Market> markets,@Size(max=50) String teamId) {}
