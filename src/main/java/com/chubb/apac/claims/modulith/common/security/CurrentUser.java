package com.chubb.apac.claims.modulith.common.security;
import com.chubb.apac.claims.modulith.common.enums.*;
import java.util.Set;
public record CurrentUser(String userId,String email,Set<UserRole> roles,Set<Market> markets,String teamId){public CurrentUser{roles=Set.copyOf(roles);markets=Set.copyOf(markets);}}
