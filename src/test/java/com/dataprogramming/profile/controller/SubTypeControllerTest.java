package com.dataprogramming.profile.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.service.SubTypeService;
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

import java.util.List;


@ExtendWith(MockitoExtension.class)
class SubTypeControllerTest {

    @InjectMocks
    private SubTypeController subTypeController;

    @Mock
    private SubTypeService subTypeService;

    @Test
    @DisplayName("Return Successful When Create SubType")
    void returnSuccessfulWhenCreateSubType() {

        SubType input = new SubType();
        input.setValue(SubType.EnumSubType.NORMAL);

        SubType saved = new SubType();
        saved.setId("68b1291b753821d691c59c79");
        saved.setValue(SubType.EnumSubType.NORMAL);

        when(subTypeService.create(any())).thenReturn(Mono.just(saved));

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
    @DisplayName("return Error When Create SubType")
    void returnErrorWhenCreateSubType () {
        SubType input = new SubType();
        input.setValue(SubType.EnumSubType.VIP); // Valor válido

        when(subTypeService.create(any()))
                .thenReturn(Mono.error(new RuntimeException("Something went wrong")));

        Mono<ResponseEntity<SubType>> result = subTypeController.create(input);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("Something went wrong")
                )
                .verify();
    }

    @Test
    @DisplayName("Return Internal Server Error When Service Failure")
    void returnInternalServerErrorWhenServiceFailure() {

        SubType input = new SubType();
        input.setValue(SubType.EnumSubType.VIP);

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

    @Test
    @DisplayName("Return Ok When Update SubType Exists")
    void returnOkWhenUpdateSubTypeExists() {

        SubType existing = new SubType();
        existing.setId("123");
        existing.setValue(SubType.EnumSubType.NORMAL);

        SubType updated = new SubType();
        updated.setId("123");
        updated.setValue(SubType.EnumSubType.VIP);

        when(subTypeService.findById("123")).thenReturn(Mono.just(existing));
        when(subTypeService.update(any(SubType.class))).thenReturn(Mono.just(updated));

        Mono<ResponseEntity<SubType>> result = subTypeController.update(updated);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.OK &&
                                response.getBody() != null &&
                                response.getBody().getId().equals("123") &&
                                response.getBody().getValue() == SubType.EnumSubType.VIP
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Error When Update Throws Exception")
    void returnErrorWhenUpdateThrowsException() {

        SubType subType = new SubType();
        subType.setId("1");
        subType.setValue(SubType.EnumSubType.NORMAL);

        when(subTypeService.findById("1")).thenReturn(Mono.just(subType));
        when(subTypeService.update(any(SubType.class)))
                .thenReturn(Mono.error(new RuntimeException("DB error"))); // Simula error

        Mono<ResponseEntity<SubType>> result = subTypeController.update(subType);

        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof RuntimeException &&
                                throwable.getMessage().equals("DB error"))
                .verify();
    }

    @Test
    @DisplayName("Return Not Found When SubType Does Not Exist")
    void returnNotFoundWhenSubTypeDoesNotExist() {

        SubType subType = new SubType();
        subType.setId("999");
        subType.setValue(SubType.EnumSubType.PYME);

        when(subTypeService.findById("999")).thenReturn(Mono.empty());

        Mono<ResponseEntity<SubType>> result = subTypeController.update(subType);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.NOT_FOUND &&
                                response.getBody() == null
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Return NoContent When Delete SubType Exists")
    void returnNoContentWhenDeleteSubTypeExists() {
        String id = "123";
        when(subTypeService.delete(id)).thenReturn(Mono.just(true));

        Mono<ResponseEntity<Void>> result = subTypeController.delete(id);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.NO_CONTENT
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Not Found When Delete SubType")
    void returnNotFoundWhenDeleteSubType() {

        String id = "999";
        when(subTypeService.delete(id)).thenReturn(Mono.just(false));

        Mono<ResponseEntity<Void>> result = subTypeController.delete(id);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.NOT_FOUND
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Internal Server Error When Error Occurs")
    void returnInternalServerErrorWhenErrorOccurs() {

        String id = "500";
        when(subTypeService.delete(id))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<ResponseEntity<Void>> result = subTypeController.delete(id);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Successful When List SubTypes")
    void returnSuccessfulWhenListSubTypes() {

        SubType s1 = new SubType();
        s1.setId("1");
        s1.setValue(SubType.EnumSubType.NORMAL);

        SubType s2 = new SubType();
        s2.setId("2");
        s2.setValue(SubType.EnumSubType.VIP);

        when(subTypeService.findAll())
                .thenReturn(Flux.fromIterable(List.of(s1, s2)));

        Flux<SubType> result = subTypeController.list();

        StepVerifier.create(result)
                .expectNextMatches(st -> st.getId().equals("1") && st.getValue() == SubType.EnumSubType.NORMAL)
                .expectNextMatches(st -> st.getId().equals("2") && st.getValue() == SubType.EnumSubType.VIP)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Empty When No SubTypes Exist")
    void returnEmptyWhenNoSubTypesExist() {

        when(subTypeService.findAll()).thenReturn(Flux.empty());

        Flux<SubType> result = subTypeController.list();

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Bad Request When Id Is Blank")
    void returnBadRequestWhenIdIsBlank() {
        Mono<ResponseEntity<SubType>> result = subTypeController.findById("   ");

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.BAD_REQUEST &&
                                response.getBody() == null
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Ok When Find By Id SubType")
    void returnOkWhenFindByIdSubType() {
        String id = "123";
        SubType subType = new SubType();
        subType.setId(id);
        subType.setValue(SubType.EnumSubType.VIP);

        when(subTypeService.findById(id)).thenReturn(Mono.just(subType));

        Mono<ResponseEntity<SubType>> result = subTypeController.findById(id);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.OK &&
                                response.getBody() != null &&
                                response.getBody().getId().equals(id) &&
                                response.getBody().getValue() == SubType.EnumSubType.VIP
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Not Found When Not Exist Find By Id")
    void returnNotFoundWhenNotExistFindById() {
        String id = "999";
        when(subTypeService.findById(id)).thenReturn(Mono.empty());

        Mono<ResponseEntity<SubType>> result = subTypeController.findById(id);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.NOT_FOUND &&
                                response.getBody() == null
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Internal Server Error When Error Occurs FindById")
    void returnInternalServerErrorWhenErrorOccursFindById() {
        String id = "500";
        when(subTypeService.findById(id))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<ResponseEntity<SubType>> result = subTypeController.findById(id);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR &&
                                response.getBody() == null
                )
                .verifyComplete();
    }
}