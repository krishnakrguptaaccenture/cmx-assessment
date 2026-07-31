package com.chubb.apac.claims.modulith.user.controller;
import com.chubb.apac.claims.modulith.common.dto.*;
import com.chubb.apac.claims.modulith.common.security.CurrentUser;
import com.chubb.apac.claims.modulith.user.dto.request.*;
import com.chubb.apac.claims.modulith.user.dto.response.UserResponse;
import com.chubb.apac.claims.modulith.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/v1")
public class UserController {private final UserService service;public UserController(UserService s){service=s;}
 @GetMapping("/users/profile") ApiResponse<UserResponse> profile(@AuthenticationPrincipal CurrentUser u){return ApiResponse.success(service.profile(u.userId()));}
 @PutMapping("/users/profile") ApiResponse<UserResponse> update(@AuthenticationPrincipal CurrentUser u,@Valid @RequestBody UpdateProfileRequest r){return ApiResponse.success(service.updateProfile(u.userId(),r));}
 @PostMapping("/staff/users") @PreAuthorize("hasRole('MANAGER')") ResponseEntity<ApiResponse<UserResponse>> createStaff(@Valid @RequestBody CreateStaffUserRequest r){return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(service.createStaff(r)));}

 @GetMapping("/staff/users")
 @PreAuthorize("hasRole('MANAGER')")
 public PageResponse<UserResponse> listStaff(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

  Pageable pageable = PageRequest.of(page, size);
  return service.listStaff(pageable);
 }
}
