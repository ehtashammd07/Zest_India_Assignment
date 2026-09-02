package com.zest.products.common;
import jakarta.servlet.http.HttpServletRequest; import jakarta.validation.ConstraintViolationException; import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.time.Instant; import java.util.*;
@RestControllerAdvice public class ApiExceptionHandler {
 record ErrorResponse(Instant timestamp,int status,String error,String message,String path,Map<String,String> validationErrors) {}
 @ExceptionHandler(NoSuchElementException.class) ResponseEntity<ErrorResponse> notFound(NoSuchElementException e,HttpServletRequest r){return error(HttpStatus.NOT_FOUND,e.getMessage(),r,null);}
 @ExceptionHandler({IllegalArgumentException.class,ConstraintViolationException.class}) ResponseEntity<ErrorResponse> badRequest(Exception e,HttpServletRequest r){return error(HttpStatus.BAD_REQUEST,e.getMessage(),r,null);}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ErrorResponse> invalid(MethodArgumentNotValidException e,HttpServletRequest r){Map<String,String> m=new LinkedHashMap<>();e.getBindingResult().getFieldErrors().forEach(x->m.put(x.getField(),x.getDefaultMessage()));return error(HttpStatus.BAD_REQUEST,"Validation failed",r,m);}
 @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class) ResponseEntity<ErrorResponse> forbidden(Exception e,HttpServletRequest r){return error(HttpStatus.FORBIDDEN,"Access denied",r,null);}
 private ResponseEntity<ErrorResponse> error(HttpStatus s,String msg,HttpServletRequest r,Map<String,String> m){return ResponseEntity.status(s).body(new ErrorResponse(Instant.now(),s.value(),s.getReasonPhrase(),msg,r.getRequestURI(),m));}
}
