package com.chubb.apac.claims.modulith.user.dto.response;

import com.chubb.apac.claims.modulith.common.enums.UserRole;

public record AuthResponse(String token, String userId, String email, String fullName, UserRole role) {}
