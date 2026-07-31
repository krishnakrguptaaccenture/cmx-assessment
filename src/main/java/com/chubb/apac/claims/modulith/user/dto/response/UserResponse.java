package com.chubb.apac.claims.modulith.user.dto.response;
import com.chubb.apac.claims.modulith.user.model.*;
import java.time.LocalDateTime;
import java.util.Set;
public record UserResponse(String userId,String email,String fullName,String phoneNumber,
 Set<UserRole> roles,Set<Market> markets,String teamId,boolean active,LocalDateTime createdAt) {}
