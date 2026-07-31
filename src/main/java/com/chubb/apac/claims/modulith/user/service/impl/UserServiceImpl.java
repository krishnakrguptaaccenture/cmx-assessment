package com.chubb.apac.claims.modulith.user.service.impl;
import com.chubb.apac.claims.modulith.common.dto.PageResponse;
import com.chubb.apac.claims.modulith.common.enums.UserRole;
import com.chubb.apac.claims.modulith.common.exception.*;import com.chubb.apac.claims.modulith.user.dto.request.*;import com.chubb.apac.claims.modulith.user.dto.response.UserResponse;import com.chubb.apac.claims.modulith.user.mapper.UserMapper;
import com.chubb.apac.claims.modulith.user.model.User;
import com.chubb.apac.claims.modulith.user.repository.UserRepository;import com.chubb.apac.claims.modulith.user.service.UserService;import org.springframework.data.domain.*;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.util.*;
@Service @Transactional
public class UserServiceImpl implements UserService {
 private final UserRepository users;private final UserMapper mapper;private final PasswordEncoder passwords;
 public UserServiceImpl(UserRepository u,UserMapper m,PasswordEncoder p){users=u;mapper=m;passwords=p;}
 @Transactional(readOnly=true) public UserResponse profile(String id){return mapper.toResponse(find(id));}
 public UserResponse updateProfile(String id,UpdateProfileRequest r){User u=find(id);if(r.fullName()!=null)u.setFullName(r.fullName().trim());if(r.phoneNumber()!=null)u.setPhoneNumber(r.phoneNumber());return mapper.toResponse(u);}
 public UserResponse createStaff(CreateStaffUserRequest r){if(r.role()== UserRole.CLAIMANT)throw new IllegalArgumentException("Staff role must be CLAIMS_STAFF or MANAGER");String email=r.email().trim().toLowerCase(Locale.ROOT);if(users.existsByEmailIgnoreCase(email))throw new ConflictException("Email already registered");User u=new User();u.setId(UUID.randomUUID().toString());u.setEmail(email);u.setPassword(passwords.encode(r.password()));u.setFullName(r.fullName().trim());u.setPhoneNumber(r.phoneNumber());u.getRoles().add(r.role());u.getMarkets().addAll(r.markets());u.setTeamId(r.teamId());users.save(u);return mapper.toResponse(u);}
 @Transactional(readOnly=true) public PageResponse<UserResponse> listStaff(Pageable p){Page<UserResponse> page=users.findDistinctByRolesIn(java.util.Set.of(UserRole.CLAIMS_STAFF,UserRole.MANAGER),p).map(mapper::toResponse);return PageResponse.from(page);}
 private User find(String id){return users.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found"));}
}
