package com.chubb.apac.claims.modulith.user.controller;
import com.chubb.apac.claims.modulith.common.dto.ApiResponse;import com.chubb.apac.claims.modulith.user.dto.request.*;import com.chubb.apac.claims.modulith.user.dto.response.AuthResponse;import com.chubb.apac.claims.modulith.user.service.AuthService;import jakarta.validation.Valid;import org.springframework.http.*;import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/auth")
public class AuthController { private final AuthService service;public AuthController(AuthService s){service=s;}
 @PostMapping("/register") ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterClaimantRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(service.register(r)));}
 @PostMapping("/login") ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest r){return ApiResponse.success(service.login(r));}
 @PostMapping("/logout") ResponseEntity<Void> logout(@RequestHeader("Authorization") String h){service.logout(h);return ResponseEntity.noContent().build();}
}
