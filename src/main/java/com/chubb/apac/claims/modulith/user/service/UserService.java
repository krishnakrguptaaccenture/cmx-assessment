package com.chubb.apac.claims.modulith.user.service;
import com.chubb.apac.claims.modulith.common.dto.PageResponse;import com.chubb.apac.claims.modulith.user.dto.request.*;import com.chubb.apac.claims.modulith.user.dto.response.UserResponse;import org.springframework.data.domain.Pageable;
public interface UserService { UserResponse profile(String userId); UserResponse updateProfile(String userId,UpdateProfileRequest request); UserResponse createStaff(CreateStaffUserRequest request); PageResponse<UserResponse> listStaff(Pageable pageable); }
