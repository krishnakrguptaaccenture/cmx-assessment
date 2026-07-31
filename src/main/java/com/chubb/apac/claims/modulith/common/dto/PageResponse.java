package com.chubb.apac.claims.modulith.common.dto;
import org.springframework.data.domain.Page;
import java.util.List;
public record PageResponse<T>(List<T> data,Pagination pagination) {
 public record Pagination(int page,int size,long totalElements,int totalPages) {}
 public static <T> PageResponse<T> from(Page<T> page){return new PageResponse<>(page.getContent(),new Pagination(page.getNumber(),page.getSize(),page.getTotalElements(),page.getTotalPages()));}
}
