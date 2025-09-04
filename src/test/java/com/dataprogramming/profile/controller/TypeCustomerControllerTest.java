package com.dataprogramming.profile.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.entity.TypeCustomer;
import com.dataprogramming.profile.service.TypeCustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class TypeCustomerControllerTest {

    @InjectMocks
    private TypeCustomerController typeCustomerController;

    @Mock
    private TypeCustomerService typeCustomerService;

    private TypeCustomer customer;

    @BeforeEach
    void setUp() {
        customer = new TypeCustomer();
        customer.setId("1");
        customer.setValue(TypeCustomer.EnumTypeCustomer.BUSINESS);
    }

    @Test
    @DisplayName("Return Successful When List TypeCustomers")
    void returnSuccessfulWhenListTypeCustomers() {
        when(typeCustomerService.findAll()).thenReturn(Flux.just(customer));

        Flux<TypeCustomer> result = typeCustomerController.list();

        StepVerifier.create(result)
                .expectNext(customer)
                .verifyComplete();

        verify(typeCustomerService, times(1)).findAll();
    }

    @Test
    @DisplayName("Return Successful When FindById")
    void returnSuccessfulWhenFindById() {
        when(typeCustomerService.findById("1")).thenReturn(Mono.just(customer));

        Mono<ResponseEntity<TypeCustomer>> result = typeCustomerController.findById("1");

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.OK &&
                                response.getBody().equals(customer))
                .verifyComplete();

        verify(typeCustomerService, times(1)).findById("1");
    }

    @Test
    @DisplayName("Return BadRequest When Find By Id With Empty Id")
    void returnBadRequestWhenFindByIdWithEmptyId() {
        Mono<ResponseEntity<TypeCustomer>> result = typeCustomerController.findById("");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();

        verify(typeCustomerService, never()).findById(anyString());
    }

    @Test
    @DisplayName("Return Not Found When Find By Id Does  NotExist")
    void returnNotFoundWhenFindByIdDoesNotExist() {
        when(typeCustomerService.findById("99")).thenReturn(Mono.empty());

        Mono<ResponseEntity<TypeCustomer>> result = typeCustomerController.findById("99");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();

        verify(typeCustomerService, times(1)).findById("99");
    }

    @Test
    @DisplayName("Return Internal Server Error When Find By Id Throws Exception")
    void returnInternalServerErrorWhenFindByIdThrowsException() {
        when(typeCustomerService.findById("1")).thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<ResponseEntity<TypeCustomer>> result = typeCustomerController.findById("1");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();

        verify(typeCustomerService, times(1)).findById("1");
    }

    @Test
    @DisplayName("Return Created When Create TypeCustomer With Valid SubType")
    void returnCreatedWhenCreateTypeCustomerWithValidSubType() {
        SubType mockSubType = new SubType("1", SubType.EnumSubType.PYME);
        TypeCustomer mockCustomer = new TypeCustomer();
        mockCustomer.setId("123");
        mockCustomer.setValue(TypeCustomer.EnumTypeCustomer.PERSONAL);
        mockCustomer.setSubType(mockSubType);

        when(typeCustomerService.checkSubType("1")).thenReturn(Mono.just(mockSubType));
        when(typeCustomerService.create(any(TypeCustomer.class))).thenReturn(Mono.just(mockCustomer));

        Mono<ResponseEntity<TypeCustomer>> response = typeCustomerController.create(mockCustomer);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getStatusCode() == HttpStatus.CREATED && r.getBody() != null)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return NotFound When Create TypeCustomer With Invalid SubType")
    void returnNotFoundWhenCreateTypeCustomerWithInvalidSubType() {
        TypeCustomer mockCustomer = new TypeCustomer();
        SubType invalidSubType = new SubType("999", SubType.EnumSubType.PYME);
        mockCustomer.setSubType(invalidSubType);

        when(typeCustomerService.checkSubType("999")).thenReturn(Mono.empty());

        Mono<ResponseEntity<TypeCustomer>> response = typeCustomerController.create(mockCustomer);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getStatusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();
    }

    // 🔹 UPDATE TESTS
    @Test
    @DisplayName("Return Created When Update Existing TypeCustomer")
    void returnCreatedWhenUpdateExistingTypeCustomer() {
        TypeCustomer mockCustomer = new TypeCustomer();
        mockCustomer.setId("123");
        mockCustomer.setValue(TypeCustomer.EnumTypeCustomer.BUSINESS);

        when(typeCustomerService.update(any(TypeCustomer.class))).thenReturn(Mono.just(mockCustomer));

        Mono<ResponseEntity<TypeCustomer>> response = typeCustomerController.update(mockCustomer);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getStatusCode() == HttpStatus.CREATED && r.getBody().equals(mockCustomer))
                .verifyComplete();
    }

    @Test
    @DisplayName("Return NotFound When Update NonExisting TypeCustomer")
    void returnNotFoundWhenUpdateNonExistingTypeCustomer() {
        TypeCustomer mockCustomer = new TypeCustomer();
        mockCustomer.setId("999");

        when(typeCustomerService.update(any(TypeCustomer.class))).thenReturn(Mono.empty());

        Mono<ResponseEntity<TypeCustomer>> response = typeCustomerController.update(mockCustomer);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getStatusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();
    }

    // 🔹 DELETE TESTS
    @Test
    @DisplayName("Return NoContent When Delete Existing TypeCustomer")
    void returnNoContentWhenDeleteExistingTypeCustomer() {
        when(typeCustomerService.delete("123")).thenReturn(Mono.just(true));

        Mono<ResponseEntity<Void>> response = typeCustomerController.delete("123");

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getStatusCode() == HttpStatus.NO_CONTENT)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return NotFound When Delete NonExisting TypeCustomer")
    void returnNotFoundWhenDeleteNonExistingTypeCustomer() {
        when(typeCustomerService.delete("999")).thenReturn(Mono.just(false));

        Mono<ResponseEntity<Void>> response = typeCustomerController.delete("999");

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getStatusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return InternalServerError When Delete Throws Exception")
    void returnInternalServerErrorWhenDeleteThrowsException() {
        when(typeCustomerService.delete("123")).thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<ResponseEntity<Void>> response = typeCustomerController.delete("123");

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();
    }
}