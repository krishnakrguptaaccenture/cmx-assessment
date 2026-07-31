package com.chubb.apac.claims.modulith.user.service;
import com.chubb.apac.claims.modulith.user.dto.request.*;import com.chubb.apac.claims.modulith.user.dto.response.AuthResponse;
public interface AuthService { AuthResponse register(RegisterClaimantRequest request); AuthResponse login(LoginRequest request); void logout(String bearerToken); }
