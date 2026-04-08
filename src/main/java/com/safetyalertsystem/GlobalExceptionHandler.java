package com.safetyalertsystem;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public static ResponseEntity<?> handleException(Exception e) {

        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());

        System.err.println("An error occurred: " + e.getMessage());

        return ResponseEntity.badRequest().body(error);
    }
}
