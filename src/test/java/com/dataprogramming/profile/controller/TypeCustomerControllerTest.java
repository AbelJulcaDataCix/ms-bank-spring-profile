package com.dataprogramming.profile.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dataprogramming.profile.dto.CustomerTypeRequest;
import com.dataprogramming.profile.dto.CustomerTypeResponse;
import com.dataprogramming.profile.dto.CustomerTypeUpdateRequest;
import com.dataprogramming.profile.entity.CustomerType;
import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.mapper.CustomerTypeMapper;
import com.dataprogramming.profile.model.EnumSubType;
import com.dataprogramming.profile.model.EnumCustomerType;
import com.dataprogramming.profile.service.CustomerTypeService;
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

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class TypeCustomerControllerTest {

    @InjectMocks
    private TypeCustomerController typeCustomerController;

    @Mock
    private CustomerTypeService typeCustomerService;

    @Mock
    private CustomerTypeMapper customerTypeMapper;

    private CustomerType customer;

    @BeforeEach
    void setUp() {
        customer = new CustomerType();
        customer.setId("1");
        customer.setValue(EnumCustomerType.BUSINESS);
    }

    @Test
    @DisplayName("Return Successful When List TypeCustomers")
    void returnSuccessfulWhenListTypeCustomers() {

        CustomerTypeResponse customerResponse = CustomerTypeResponse.builder()
                .id("1")
                .value(EnumCustomerType.BUSINESS.name())
                .subType(null)
                .build();
        when(typeCustomerService.findAll()).thenReturn(Flux.just(customer));
        when(customerTypeMapper.toTypeCustomerResponse(any())).thenReturn(customerResponse);

        Flux<CustomerTypeResponse> result = typeCustomerController.list();

        StepVerifier.create(result)
                .expectNext(customerResponse)
                .verifyComplete();

        verify(typeCustomerService, times(1)).findAll();
    }

    @Test
    @DisplayName("Return Successful When FindById")
    void returnSuccessfulWhenFindById() {
        when(typeCustomerService.findById(anyString())).thenReturn(Mono.just(customer));
        CustomerTypeResponse customerResponse = CustomerTypeResponse.builder()
                .id("1")
                .value(EnumCustomerType.BUSINESS.name())
                .subType(null)
                .build();
        when(customerTypeMapper.toTypeCustomerResponse(any())).thenReturn(customerResponse);

        Mono<ResponseEntity<CustomerTypeResponse>> result = typeCustomerController.findById("1");

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.OK &&
                                Objects.equals(response.getBody(), customerResponse))
                .verifyComplete();

        verify(typeCustomerService, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Return BadRequest When Find By Id With Empty Id")
    void returnBadRequestWhenFindByIdWithEmptyId() {

        Mono<ResponseEntity<CustomerTypeResponse>> result = typeCustomerController.findById("");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST)
                .verifyComplete();

        verify(typeCustomerService, never()).findById(anyString());
    }

    @Test
    @DisplayName("Return Not Found When Find By Id Does  NotExist")
    void returnNotFoundWhenFindByIdDoesNotExist() {
        when(typeCustomerService.findById(anyString())).thenReturn(Mono.empty());

        Mono<ResponseEntity<CustomerTypeResponse>> result = typeCustomerController.findById("99");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();

        verify(typeCustomerService, times(1)).findById(anyString());
    }

    @Test
    @DisplayName("Return Internal Server Error When Find By Id Throws Exception")
    void returnInternalServerErrorWhenFindByIdThrowsException() {
        when(typeCustomerService.findById(anyString())).thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<ResponseEntity<CustomerTypeResponse>> result = typeCustomerController.findById("1");

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
                .verifyComplete();

        verify(typeCustomerService, times(1)).findById("1");
    }

    @Test
    @DisplayName("Return Created When Create TypeCustomer With Valid SubType")
    void returnCreatedWhenCreateTypeCustomerWithValidSubType() {
        SubType mockSubType = new SubType("1", EnumSubType.PYME);
        CustomerType mockCustomer = new CustomerType();
        mockCustomer.setId("123");
        mockCustomer.setValue(EnumCustomerType.PERSONAL);
        mockCustomer.setSubType(mockSubType);

        CustomerTypeRequest request = CustomerTypeRequest.builder()
                .value("PERSONAL")
                .subType(mockSubType)
                .build();

        when(customerTypeMapper.toCustomerType(any(CustomerTypeRequest.class))).thenReturn(mockCustomer);
        when(customerTypeMapper.toTypeCustomerResponse(any(CustomerType.class)))
                .thenReturn(CustomerTypeResponse.builder()
                        .id("123")
                        .value(EnumCustomerType.PERSONAL.name())
                        .subType(null)
                        .build());
        when(typeCustomerService.checkSubType("1")).thenReturn(Mono.just(mockSubType));
        when(typeCustomerService.create(any(CustomerType.class))).thenReturn(Mono.just(mockCustomer));

        Mono<ResponseEntity<CustomerTypeResponse>> response = typeCustomerController.create(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getStatusCode() == HttpStatus.CREATED && r.getBody() != null)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return NotFound When Create TypeCustomer With Invalid SubType")
    void returnNotFoundWhenCreateTypeCustomerWithInvalidSubType() {
        CustomerType mockCustomer = new CustomerType();
        SubType invalidSubType = new SubType("999", EnumSubType.PYME);
        mockCustomer.setSubType(invalidSubType);

        SubType mockSubType = new SubType("1", EnumSubType.PYME);
        CustomerTypeRequest request = CustomerTypeRequest.builder()
                .value("PERSONAL")
                .subType(mockSubType)
                .build();

        when(typeCustomerService.checkSubType(anyString())).thenReturn(Mono.empty());

        Mono<ResponseEntity<CustomerTypeResponse>> response = typeCustomerController.create(request);

        StepVerifier.create(response)
                .expectNextMatches(r -> r.getStatusCode() == HttpStatus.NOT_FOUND)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Created When Update Existing TypeCustomer")
    void returnCreatedWhenUpdateExistingTypeCustomer() {
        CustomerType mockCustomer = new CustomerType();
        mockCustomer.setId("123");
        mockCustomer.setValue(EnumCustomerType.BUSINESS);

        CustomerTypeUpdateRequest request = CustomerTypeUpdateRequest.builder()
                .id("123")
                .value("BUSINESS")
                .build();
        CustomerTypeResponse customerTypeResponse = CustomerTypeResponse.builder()
                .id("123")
                .value(EnumCustomerType.BUSINESS.name())
                .build();

        when(customerTypeMapper.toCustomerTypeUpdate(any(CustomerTypeUpdateRequest.class))).thenReturn(mockCustomer);
        when(customerTypeMapper.toTypeCustomerResponse(any(CustomerType.class)))
                .thenReturn(customerTypeResponse);

        when(typeCustomerService.update(any(CustomerType.class))).thenReturn(Mono.just(mockCustomer));

        Mono<ResponseEntity<CustomerTypeResponse>> response = typeCustomerController.update(request);

        StepVerifier.create(response)
                .expectNextMatches(r ->
                        r.getStatusCode() == HttpStatus.CREATED && r.getBody().equals(customerTypeResponse))
                .verifyComplete();
    }

    @Test
    @DisplayName("Return NotFound When Update NonExisting TypeCustomer")
    void returnNotFoundWhenUpdateNonExistingTypeCustomer() {
        CustomerType mockCustomer = new CustomerType();
        mockCustomer.setId("999");

        CustomerTypeUpdateRequest request = CustomerTypeUpdateRequest.builder()
                .id("123")
                .value("BUSINESS")
                .subType(null)
                .build();

        when(customerTypeMapper.toCustomerTypeUpdate(any(CustomerTypeUpdateRequest.class))).thenReturn(mockCustomer);

        when(typeCustomerService.update(any(CustomerType.class))).thenReturn(Mono.empty());

        Mono<ResponseEntity<CustomerTypeResponse>> response = typeCustomerController.update(request);

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