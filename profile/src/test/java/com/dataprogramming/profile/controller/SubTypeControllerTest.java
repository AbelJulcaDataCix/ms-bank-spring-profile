package com.dataprogramming.profile.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.service.SubTypeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.AssertionErrors;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
class SubTypeControllerTest {

    @InjectMocks
    private SubTypeController subTypeController;

    @Mock
    private SubTypeService subTypeService;

    @Test
    void testCreateSubType() {

        SubType input = new SubType();
        input.setValue(SubType.EnumSubType.NORMAL);

        SubType saved = new SubType();
        saved.setId("68b1291b753821d691c59c79");
        saved.setValue(SubType.EnumSubType.NORMAL);

        when(subTypeService.create(any()))
                .thenReturn(Mono.just(saved));


        Mono<ResponseEntity<SubType>> responseMono = subTypeController.create(input);


        StepVerifier.create(responseMono)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.CREATED &&
                                response.getBody() != null &&
                                "68b1291b753821d691c59c79".equals(response.getBody().getId())
                )
                .verifyComplete();
    }

    @Test
    void testCreateSubType_error() {
        // Arrange
        SubType input = new SubType();
        input.setValue(SubType.EnumSubType.VIP); // Valor válido

        when(subTypeService.create(any()))
                .thenReturn(Mono.error(new RuntimeException("Something went wrong")));

        // Act
        Mono<ResponseEntity<SubType>> result = subTypeController.create(input);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Something went wrong")
                )
                .verify();
    }

    @Test
    void testCreateSubType_shouldReturnInternalServerErrorOnServiceFailure() {

        SubType input = new SubType();
        input.setValue(SubType.EnumSubType.VIP); // Campo obligatorio no nulo

        String errorMessage = "Database connection failed";
        when(subTypeService.create(any()))
                .thenReturn(Mono.error(new RuntimeException(errorMessage)));


        Mono<ResponseEntity<SubType>> responseMono = subTypeController.create(input);

        StepVerifier.create(responseMono)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Database connection failed")
                )
                .verify();
    }
}