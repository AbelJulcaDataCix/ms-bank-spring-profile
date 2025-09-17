package com.dataprogramming.profile.service.impl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.dataprogramming.profile.entity.CustomerType;
import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.model.EnumCustomerType;
import com.dataprogramming.profile.repository.TypeCustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class TypeCustomerServiceImplTest {

    @Mock
    private TypeCustomerRepository typeCustomerRepository;

    @Mock
    private SubTypeServiceImpl subTypeServiceImpl;

    @InjectMocks
    private CustomerTypeServiceImpl typeCustomerService;

    private CustomerType typeCustomer;

    @BeforeEach
    void setUp() {
        typeCustomer = new CustomerType();
        typeCustomer.setId("1");
        typeCustomer.setValue(EnumCustomerType.PERSONAL);
        typeCustomer.setSubType(new SubType());
    }

    @Test
    @DisplayName("Return Successful When Create TypeCustomer")
    void returnSuccessfulWhenCreateTypeCustomer() {
        when(typeCustomerRepository.save(any(CustomerType.class))).thenReturn(Mono.just(typeCustomer));

        StepVerifier.create(typeCustomerService.create(typeCustomer))
                .expectNextMatches(savedTypeCustomer ->
                        savedTypeCustomer.getId().equals("1") &&
                                savedTypeCustomer.getValue().equals(EnumCustomerType.PERSONAL))
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Successful When Find All TypeCustomer")
    void returnSuccessfulWhenFindAllTypeCustomer() {
        when(typeCustomerRepository.findAll()).thenReturn(Flux.just(typeCustomer));

        StepVerifier.create(typeCustomerService.findAll())
                .expectNextMatches(foundTypeCustomer -> foundTypeCustomer.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Successful When Find By Id TypeCustomer")
    void returnSuccessfulWhenFindByIdTypeCustomer() {
        when(typeCustomerRepository.findById(anyString())).thenReturn(Mono.just(typeCustomer));

        StepVerifier.create(typeCustomerService.findById("1"))
                .expectNextMatches(foundTypeCustomer -> foundTypeCustomer.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Not Found When Find By Id TypeCustomer")
    void returnNotFoundWhenFindByIdTypeCustomer() {
        when(typeCustomerRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(typeCustomerService.findById("2"))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Successful When Update TypeCustomer")
    void returnSuccessfulWhenUpdateTypeCustomer() {
        when(typeCustomerRepository.save(any(CustomerType.class))).thenReturn(Mono.just(typeCustomer));

        StepVerifier.create(typeCustomerService.update(typeCustomer))
                .expectNextMatches(updatedTypeCustomer -> updatedTypeCustomer.getId().equals("1"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Successful When Delete TypeCustomer")
    void returnSuccessfulWhenDeleteTypeCustomer() {
        when(typeCustomerRepository.findById("1")).thenReturn(Mono.just(typeCustomer));
        when(typeCustomerRepository.delete(any(CustomerType.class))).thenReturn(Mono.empty());

        StepVerifier.create(typeCustomerService.delete("1"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Not Found When Delete TypeCustomer")
    void returnNotFoundWhenDeleteTypeCustomer() {
        when(typeCustomerRepository.findById("2")).thenReturn(Mono.empty());

        StepVerifier.create(typeCustomerService.delete("2"))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Successful When CheckSubType")
    void returnSuccessfulWhenCheckSubType() {
        SubType subType = new SubType();
        subType.setId("sub_1");
        when(subTypeServiceImpl.findById(anyString())).thenReturn(Mono.just(subType));

        StepVerifier.create(typeCustomerService.checkSubType("sub_1"))
                .expectNextMatches(foundSubType -> foundSubType.getId().equals("sub_1"))
                .verifyComplete();
    }
}