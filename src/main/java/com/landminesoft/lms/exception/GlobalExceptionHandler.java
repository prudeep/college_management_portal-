package com.landminesoft.lms.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 401 - Invalid credentials
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidCredentials(
            InvalidCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials", ex.getMessage());
    }

    // 401 - Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorized(
            UnauthorizedException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage());
    }

    // 403 - Forbidden
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Map<String, Object>> handleForbidden(
            ForbiddenException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Forbidden", ex.getMessage());
    }

    // 409 - User already exists
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(
            UserAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, "User already exists", ex.getMessage());
    }

    // 401 - Expired JWT
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Map<String, Object>> handleExpiredJwt(
            ExpiredJwtException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Token expired",
                "Your session has expired. Please login again.");
    }

    // 401 - Invalid JWT
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, Object>> handleJwtException(
            JwtException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid token",
                "Token is invalid or malformed.");
    }

    // 403 - Access denied (wrong role)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Access denied",
                "You don't have permission to access this resource.");
    }

    // 400 - Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now().toString());
        errors.put("status", 400);
        errors.put("error", "Validation failed");
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage())
        );
        errors.put("messages", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 409 - Duplicate/conflict from RuntimeException
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("already exists")) {
            return buildResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
        }
        if (ex.getMessage() != null && ex.getMessage().contains("Invalid email or password")) {
            return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials", ex.getMessage());
        }
        if (ex.getMessage() != null && ex.getMessage().contains("not found")) {
            return buildResponse(HttpStatus.NOT_FOUND, "Not found", ex.getMessage());
        }
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}