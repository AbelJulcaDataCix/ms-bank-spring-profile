package com.dataprogramming.profile.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ApiException> handleServerWebInputException(ServerWebInputException ex) {
        log.error("Error in request body: {}", ex.getReason(), ex);
        String errorMessage = "Request body is invalid or malformed.";
        ApiException apiError = new ApiException(errorMessage, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ApiException> handleWebExchangeBindException(WebExchangeBindException ex) {
        log.error("Validation failed: {}", ex.getBindingResult().getAllErrors().get(0).getDefaultMessage(), ex);
        String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        ApiException apiError = new ApiException(errorMessage, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("An unhandled exception occurred:", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }
}
