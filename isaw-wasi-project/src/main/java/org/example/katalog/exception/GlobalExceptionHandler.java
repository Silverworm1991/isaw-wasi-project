package org.example.katalog.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Handles @Valid annotation failures (e.g., blank title, negative year)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error -> {
              String field =
                  (error instanceof FieldError) ? ((FieldError) error).getField() : "general";
              errors.put(field, error.getDefaultMessage());
            });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  // Handles missing or invalid "type" field in JSON body (Jackson deserialization failure)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleMalformedRequest(
      HttpMessageNotReadableException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Invalid or missing 'type' field. Must be 'movie' or 'series'.");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }
}
