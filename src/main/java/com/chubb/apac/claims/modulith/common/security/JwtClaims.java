package com.chubb.apac.claims.modulith.common.security;
import com.chubb.apac.claims.modulith.common.enums.*;
import java.util.Set;
public record JwtClaims(String userId,String email,Set<UserRole> roles,Set<Market> markets,String teamId) {}
