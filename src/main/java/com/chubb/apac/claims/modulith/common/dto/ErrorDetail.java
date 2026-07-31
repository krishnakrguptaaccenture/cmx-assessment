package com.chubb.apac.claims.modulith.common.dto;
import java.util.List;
public record ErrorDetail(String code,String message,List<FieldViolation> details){public ErrorDetail{details=details==null?List.of():List.copyOf(details);}}
