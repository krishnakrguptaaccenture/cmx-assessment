package com.chubb.apac.claims.modulith.user.mapper;
import com.chubb.apac.claims.modulith.user.dto.response.UserResponse;
import com.chubb.apac.claims.modulith.user.model.User;
import org.springframework.stereotype.Component;
import java.util.Set;
@Component
public class UserMapper {
 public UserResponse toResponse(User u){return new UserResponse(u.getId(),u.getEmail(),u.getFullName(),u.getPhoneNumber(),Set.copyOf(u.getRoles()),Set.copyOf(u.getMarkets()),u.getTeamId(),u.isActive(),u.getCreatedAt());}
}
