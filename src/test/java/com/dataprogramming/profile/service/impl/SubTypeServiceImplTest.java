package com.dataprogramming.profile.service.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.repository.SubTypeRepository;
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
class SubTypeServiceImplTest {

    @Mock
    private SubTypeRepository subTypeRepository;

    @InjectMocks
    private SubTypeServiceImpl subTypeService;

    private SubType subType;

    @BeforeEach
    void setUp() {
        subType = new SubType();
        subType.setId("1");
        subType.setValue(SubType.EnumSubType.NORMAL);
    }

    @Test
    @DisplayName("Return Successful When Create SubTypeService")
    void returnSuccessfulWhenCreateSubTypeService() {

        when(subTypeRepository.save(any(SubType.class))).thenReturn(Mono.just(subType));

        StepVerifier.create(subTypeService.create(subType))
                .expectNext(subType)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Successful When Find All SubTypeService")
    void returnSuccessfulWhenFindAllSubTypeService() {

        when(subTypeRepository.findAll()).thenReturn(Flux.just(subType));

        StepVerifier.create(subTypeService.findAll())
                .expectNext(subType)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Successful When Find By Id SubTypeService")
    void returnSuccessfulWhenFindByIdSubTypeService() {

        when(subTypeRepository.findById(anyString())).thenReturn(Mono.just(subType));

        StepVerifier.create(subTypeService.findById("1"))
                .expectNext(subType)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Not Found When Find By Id SubTypeService")
    void returnNotFoundWhenFindByIdSubTypeService() {

        when(subTypeRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(subTypeService.findById("2"))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Successful When Update SubTypeService")
    void returnSuccessfulWhenUpdateSubTypeService() {

        when(subTypeRepository.save(any(SubType.class))).thenReturn(Mono.just(subType));

        StepVerifier.create(subTypeService.update(subType))
                .expectNext(subType)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Successful When Delete SubTypeService")
    void returnSuccessfulWhenDeleteSubTypeService() {

        when(subTypeRepository.findById(anyString())).thenReturn(Mono.just(subType));
        when(subTypeRepository.delete(any(SubType.class))).thenReturn(Mono.empty());

        StepVerifier.create(subTypeService.delete("1"))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Not Found When Delete SubTypeService")
    void returnNotFoundWhenDeleteSubTypeService() {

        when(subTypeRepository.findById(anyString())).thenReturn(Mono.empty());

        StepVerifier.create(subTypeService.delete("2"))
                .expectNext(false)
                .verifyComplete();
    }
}
