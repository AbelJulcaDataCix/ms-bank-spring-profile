package com.dataprogramming.profile.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ApiException {
    private String message;
    private HttpStatus status;
    private int statusCode;
    private LocalDateTime timestamp;

    public ApiException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.statusCode = status.value();
        this.timestamp = LocalDateTime.now();
    }
}
