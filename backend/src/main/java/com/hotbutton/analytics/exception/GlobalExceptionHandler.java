package com.hotbutton.analytics.exception;

import com.hotbutton.analytics.dto.ErrorResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AnalyticsException.class)
    public ResponseEntity<ErrorResponse> handleAnalytics(
            AnalyticsException ex) {

        return ResponseEntity.badRequest()
                .body(
                        ErrorResponse.builder()
                                .message(ex.getMessage())
                                .code(ex.getCode())
                                .timestamp(System.currentTimeMillis())
                                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        String message =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .findFirst()
                        .map(error ->
                                error.getField()
                                        + " "
                                        + error.getDefaultMessage())
                        .orElse("Invalid request");

        return ResponseEntity.badRequest()
                .body(
                        ErrorResponse.builder()
                                .message(message)
                                .code("VALIDATION_ERROR")
                                .timestamp(System.currentTimeMillis())
                                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex) {

        log.error("Unhandled analytics error", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ErrorResponse.builder()
                                .message("Analytics query failed")
                                .code("INTERNAL_ERROR")
                                .timestamp(System.currentTimeMillis())
                                .build());
    }
}