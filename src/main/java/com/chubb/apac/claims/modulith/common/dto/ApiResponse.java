package com.chubb.apac.claims.modulith.common.dto;
import java.time.Instant;
public record ApiResponse<T>(boolean success,T data,Instant timestamp) {
 public static <T> ApiResponse<T> success(T data){ return new ApiResponse<>(true,data,Instant.now()); }
}
