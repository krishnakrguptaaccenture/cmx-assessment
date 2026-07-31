package com.chubb.apac.claims.modulith.user.service.impl;
import com.chubb.apac.claims.modulith.common.enums.UserRole;
import com.chubb.apac.claims.modulith.common.exception.*;
import com.chubb.apac.claims.modulith.common.security.JwtClaims;
import com.chubb.apac.claims.modulith.common.security.JwtService;
import com.chubb.apac.claims.modulith.user.dto.request.*;
import com.chubb.apac.claims.modulith.user.dto.response.AuthResponse;
import com.chubb.apac.claims.modulith.user.model.User;
import com.chubb.apac.claims.modulith.user.repository.UserRepository;
import com.chubb.apac.claims.modulith.user.service.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service @Transactional
public class AuthServiceImpl implements AuthService {
 private final UserRepository users;private final PasswordEncoder passwords;private final JwtService jwt;private final TokenRevocationService revocations;
 public AuthServiceImpl(UserRepository u,PasswordEncoder p,JwtService j,TokenRevocationService r){users=u;passwords=p;jwt=j;revocations=r;}
 public AuthResponse register(RegisterClaimantRequest r){String email=r.email().trim().toLowerCase(Locale.ROOT);if(users.existsByEmailIgnoreCase(email))throw new ConflictException("Email already registered");User u=new User();u.setId(UUID.randomUUID().toString());u.setEmail(email);u.setPassword(passwords.encode(r.password()));u.setFullName(r.fullName().trim());u.setPhoneNumber(r.phoneNumber());u.getRoles().add(UserRole.CLAIMANT);users.save(u);return auth(u);}
 @Transactional(readOnly = true)
 public AuthResponse login(LoginRequest request) {

  User user = users.findByEmailIgnoreCase(request.email())
          .filter(User::isActive)
          .orElseThrow(
                  () -> new UnauthorisedException(
                          "Invalid credentials"));

  if (!passwords.matches(
          request.password(),
          user.getPassword())) {

   throw new UnauthorisedException(
           "Invalid credentials");
  }

  return auth(user);
 }

 public void logout(String header) {

  if (header == null || !header.startsWith("Bearer ")) {
   throw new UnauthorisedException(
           "Missing bearer token");
  }

  String token = header.substring(7);

  revocations.revoke(
          token,
          jwt.expiry(token));
 }

 private AuthResponse auth(User user) {

  String token = jwt.generate(
          new JwtClaims(
                  user.getId(),
                  user.getEmail(),
                  user.getRoles(),
                  user.getMarkets(),
                  user.getTeamId()));

  UserRole role = user.getRoles()
          .stream()
          .findFirst()
          .orElse(UserRole.CLAIMANT);

  return new AuthResponse(
          token,
          user.getId(),
          user.getEmail(),
          user.getFullName(),
          role);
 }
}
