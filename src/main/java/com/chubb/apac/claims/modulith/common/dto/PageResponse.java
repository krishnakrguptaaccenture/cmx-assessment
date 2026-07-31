package com.chubb.apac.claims.modulith.common.dto;
import org.springframework.data.domain.Page;
import java.util.List;
public record PageResponse<T>(List<T> data,Pagination pagination){
 public record Pagination(int page,int size,long totalElements,int totalPages){}
 public static <T> PageResponse<T> from(Page<T> p){return new PageResponse<>(List.copyOf(p.getContent()),new Pagination(p.getNumber(),p.getSize(),p.getTotalElements(),p.getTotalPages()));}
}
