package com.energytop.energytop_backend.common.globalExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
      Map<String, Object> response = new HashMap<>();
      
      // Agregar el campo de error y el mensaje general
      response.put("error", "Validation Error");
      response.put("message", "One or more fields are invalid.");

      // Agregar los errores de campo
      Map<String, String> fieldErrors = new HashMap<>();
      ex.getBindingResult().getFieldErrors().forEach(error -> 
          fieldErrors.put(error.getField(), error.getDefaultMessage())
      );

      response.put("fieldErrors", fieldErrors);

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", "Request processing failed");
    errorResponse.put("message", ex.getMessage()); // Retornar el mensaje de la excepción

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException ex) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("error", "Request processing failed");
    errorResponse.put("message", ex.getMessage()); 
    return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
  }
}