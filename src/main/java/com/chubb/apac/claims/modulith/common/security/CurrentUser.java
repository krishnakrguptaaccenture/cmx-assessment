package com.chubb.apac.claims.modulith.common.security;
import java.util.Set;
public record CurrentUser(String userId,String email,Set<String> roles,Set<String> markets,String teamId) {}
