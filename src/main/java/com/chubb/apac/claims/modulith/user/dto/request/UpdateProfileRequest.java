package com.chubb.apac.claims.modulith.user.dto.request;
import jakarta.validation.constraints.Size;
public record UpdateProfileRequest(@Size(min=1,max=150) String fullName,@Size(max=30) String phoneNumber) {}
