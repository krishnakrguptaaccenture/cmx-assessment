package com.chubb.apac.claims.modulith.common.exception;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;
@RestControllerAdvice
public class GlobalExceptionHandler {
 public record FieldError(String field,String message){}
 public record ErrorDetail(String code,String message,List<FieldError> details){}
 public record ErrorResponse(boolean success,ErrorDetail error,Instant timestamp){}
 private ResponseEntity<ErrorResponse> response(HttpStatus s,String code,String msg,List<FieldError> d){return ResponseEntity.status(s).body(new ErrorResponse(false,new ErrorDetail(code,msg,d),Instant.now()));}
 @ExceptionHandler(MethodArgumentNotValidException.class)
 ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException e){var d=e.getBindingResult().getFieldErrors().stream().map(x->new FieldError(x.getField(),x.getDefaultMessage())).toList();return response(HttpStatus.BAD_REQUEST,"VALIDATION_ERROR","Request validation failed",d);}
 @ExceptionHandler(ResourceNotFoundException.class) ResponseEntity<ErrorResponse> notFound(ResourceNotFoundException e){return response(HttpStatus.NOT_FOUND,"NOT_FOUND",e.getMessage(),List.of());}
 @ExceptionHandler(ConflictException.class) ResponseEntity<ErrorResponse> conflict(ConflictException e){return response(HttpStatus.CONFLICT,"CONFLICT",e.getMessage(),List.of());}
 @ExceptionHandler(UnauthorizedException.class) ResponseEntity<ErrorResponse> unauth(UnauthorizedException e){return response(HttpStatus.UNAUTHORIZED,"UNAUTHORIZED",e.getMessage(),List.of());}
 @ExceptionHandler(ForbiddenException.class) ResponseEntity<ErrorResponse> forbidden(ForbiddenException e){return response(HttpStatus.FORBIDDEN,"FORBIDDEN",e.getMessage(),List.of());}
}
