package com.dataprogramming.profile.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    @DisplayName("Return BadRequest When ServerWebInputException Is Thrown")
    void returnBadRequestWhenServerWebInputExceptionIsThrown() {

        ServerWebInputException ex = new ServerWebInputException("Invalid body");

        ResponseEntity<ControlError> response = handler.handleServerWebInputException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Request body is invalid or malformed.", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
        assertEquals(400, response.getBody().getStatusCode());
    }

    @Test
    @DisplayName("Return BadRequest When WebExchangeBindException Is Thrown")
    void returnBadRequestWhenWebExchangeBindExceptionIsThrown() throws NoSuchMethodException {

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(new ObjectError("testObject", "Field X is required"));

        Method method = Object.class.getMethod("toString");
        MethodParameter methodParameter = new MethodParameter(method, -1);

        WebExchangeBindException ex =
                new WebExchangeBindException(methodParameter, bindingResult);

        ResponseEntity<ControlError> response = handler.handleWebExchangeBindException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Field X is required", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
    }


    @Test
    @DisplayName("Return InternalServerError When Generic Exception Is Thrown")
    void returnInternalServerErrorWhenGenericExceptionIsThrown() {

        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<String> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred. Please try again later.", response.getBody());
    }
}