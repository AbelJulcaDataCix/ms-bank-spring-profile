package com.dataprogramming.profile.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ControlError {
    private String message;
    private HttpStatus status;
    private int statusCode;
    private LocalDateTime timestamp;

    public ControlError(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.statusCode = status.value();
        this.timestamp = LocalDateTime.now();
    }
}
