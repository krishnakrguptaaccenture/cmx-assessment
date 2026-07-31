package com.chubb.apac.claims.modulith.common.exception;
import com.chubb.apac.claims.modulith.common.dto.*;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestControllerAdvice
public class GlobalExceptionHandler {
 private ResponseEntity<ApiResponse<Void>> error(HttpStatus s,String c,String m,List<FieldViolation> d){return ResponseEntity.status(s).body(ApiResponse.failure(new ErrorDetail(c,m,d)));}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ApiResponse<Void>> invalid(MethodArgumentNotValidException e){var d=e.getBindingResult().getFieldErrors().stream().map(x->new FieldViolation(x.getField(),x.getDefaultMessage())).toList();return error(HttpStatus.BAD_REQUEST,"VALIDATION_ERROR","Request validation failed",d);}
 @ExceptionHandler(ConstraintViolationException.class) ResponseEntity<ApiResponse<Void>> constraint(ConstraintViolationException e){var d=e.getConstraintViolations().stream().map(x->new FieldViolation(x.getPropertyPath().toString(),x.getMessage())).toList();return error(HttpStatus.BAD_REQUEST,"VALIDATION_ERROR","Request validation failed",d);}
 @ExceptionHandler({HttpMessageNotReadableException.class,MissingRequestHeaderException.class}) ResponseEntity<ApiResponse<Void>> malformed(Exception e){return error(HttpStatus.BAD_REQUEST,"INVALID_REQUEST","Request is malformed or incomplete",List.of());}
 @ExceptionHandler(ResourceNotFoundException.class) ResponseEntity<ApiResponse<Void>> notFound(ResourceNotFoundException e){return error(HttpStatus.NOT_FOUND,"NOT_FOUND",e.getMessage(),List.of());}
 @ExceptionHandler(ConflictException.class) ResponseEntity<ApiResponse<Void>> conflict(ConflictException e){return error(HttpStatus.CONFLICT,"CONFLICT",e.getMessage(),List.of());}
 @ExceptionHandler({ForbiddenException.class,AccessDeniedException.class}) ResponseEntity<ApiResponse<Void>> forbidden(Exception e){return error(HttpStatus.FORBIDDEN,"FORBIDDEN","Access is denied",List.of());}
 @ExceptionHandler(UnauthorisedException.class) ResponseEntity<ApiResponse<Void>> unauthorised(UnauthorisedException e){return error(HttpStatus.UNAUTHORIZED,"UNAUTHORISED",e.getMessage(),List.of());}
 @ExceptionHandler(BusinessValidationException.class) ResponseEntity<ApiResponse<Void>> business(BusinessValidationException e){return error(HttpStatus.BAD_REQUEST,"BUSINESS_VALIDATION_ERROR",e.getMessage(),List.of());}
 @ExceptionHandler(Exception.class) ResponseEntity<ApiResponse<Void>> unexpected(Exception e){return error(HttpStatus.INTERNAL_SERVER_ERROR,"INTERNAL_ERROR","An unexpected error occurred",List.of());}
}
