package com.dataprogramming.profile.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.dataprogramming.profile.dto.SubTypeRequest;
import com.dataprogramming.profile.dto.SubTypeResponse;
import com.dataprogramming.profile.dto.SubTypeUpdateRequest;
import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.mapper.SubTypeMapper;
import com.dataprogramming.profile.model.EnumSubType;
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

    @Mock
    private SubTypeMapper subTypeMapper;

    @Test
    @DisplayName("Return Successful When Create SubType")
    void returnSuccessfulWhenCreateSubType() {

        SubTypeRequest input = new SubTypeRequest();
        input.setValue(EnumSubType.NORMAL);

        SubType saved = new SubType();
        saved.setId("68b1291b753821d691c59c79");
        saved.setValue(EnumSubType.NORMAL);

        when(subTypeService.create(any())).thenReturn(Mono.just(saved));

        when(subTypeMapper.toSubTypeResponse(any()))
                .thenReturn(SubTypeResponse
                        .builder()
                        .id("68b1291b753821d691c59c79")
                        .value("NORMAL")
                        .build());

        Mono<ResponseEntity<SubTypeResponse>> responseMono = subTypeController.create(input);

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
        SubTypeRequest input = new SubTypeRequest();
        input.setValue(EnumSubType.VIP); // Valor válido

        when(subTypeService.create(any()))
                .thenReturn(Mono.error(new RuntimeException("Something went wrong")));

        Mono<ResponseEntity<SubTypeResponse>> result = subTypeController.create(input);

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

        SubTypeRequest input = new SubTypeRequest();
        input.setValue(EnumSubType.VIP);

        String errorMessage = "Database connection failed";
        when(subTypeService.create(any()))
                .thenReturn(Mono.error(new RuntimeException(errorMessage)));

        Mono<ResponseEntity<SubTypeResponse>> responseMono = subTypeController.create(input);

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
        existing.setValue(EnumSubType.NORMAL);

        SubType updated = new SubType();
        updated.setId("123");
        updated.setValue(EnumSubType.VIP);

        SubTypeUpdateRequest request = new SubTypeUpdateRequest();
        request.setId("123");
        request.setValue(EnumSubType.VIP);

        when(subTypeService.findById(any())).thenReturn(Mono.just(existing));
        when(subTypeService.update(any(SubType.class))).thenReturn(Mono.just(updated));
        when(subTypeMapper.toSubTypeResponse(any()))
                .thenReturn(SubTypeResponse.builder()
                        .id("123")
                        .value("VIP")
                        .build());

        Mono<ResponseEntity<SubTypeResponse>> result = subTypeController.update(request);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.OK &&
                                response.getBody() != null &&
                                "123".equals(response.getBody().getId()) &&
                                "VIP".equals(response.getBody().getValue())
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Error When Update Throws Exception")
    void returnErrorWhenUpdateThrowsException() {

        SubType subType = new SubType();
        subType.setId("1");
        subType.setValue(EnumSubType.NORMAL);
        SubTypeUpdateRequest request = new SubTypeUpdateRequest();
        request.setId("123");
        request.setValue(EnumSubType.VIP);

        when(subTypeService.findById(any())).thenReturn(Mono.just(subType));
        when(subTypeService.update(any(SubType.class)))
                .thenReturn(Mono.error(new RuntimeException("DB error"))); // Simula error

        Mono<ResponseEntity<SubTypeResponse>> result = subTypeController.update(request);

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
        subType.setValue(EnumSubType.PYME);
        SubTypeUpdateRequest request = new SubTypeUpdateRequest();
        request.setId("999");
        request.setValue(EnumSubType.VIP);

        when(subTypeService.findById(any())).thenReturn(Mono.empty());

        Mono<ResponseEntity<SubTypeResponse>> result = subTypeController.update(request);

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
        when(subTypeService.delete(any())).thenReturn(Mono.just(true));

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
        when(subTypeService.delete(any())).thenReturn(Mono.just(false));

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
        when(subTypeService.delete(any()))
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
        s1.setValue(EnumSubType.NORMAL);

        SubType s2 = new SubType();
        s2.setId("2");
        s2.setValue(EnumSubType.VIP);

        when(subTypeService.findAll())
                .thenReturn(Flux.fromIterable(List.of(s1, s2)));

        Flux<SubType> result = subTypeController.list();

        StepVerifier.create(result)
                .expectNextMatches(st -> st.getId().equals("1") && st.getValue() == EnumSubType.NORMAL)
                .expectNextMatches(st -> st.getId().equals("2") && st.getValue() == EnumSubType.VIP)
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
        Mono<ResponseEntity<SubTypeResponse>> result = subTypeController.findById("   ");

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
        subType.setValue(EnumSubType.VIP);

        when(subTypeService.findById(anyString())).thenReturn(Mono.just(subType));
        when(subTypeMapper.toSubTypeResponse(any()))
                .thenReturn(SubTypeResponse.builder()
                        .id("123")
                        .value("VIP")
                        .build());

        Mono<ResponseEntity<SubTypeResponse>> result = subTypeController.findById(id);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.OK &&
                                response.getBody() != null &&
                                "123".equals(response.getBody().getId()) &&
                                "VIP".equals(response.getBody().getValue())
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Return Not Found When Not Exist Find By Id")
    void returnNotFoundWhenNotExistFindById() {
        String id = "999";
        when(subTypeService.findById(any())).thenReturn(Mono.empty());

        Mono<ResponseEntity<SubTypeResponse>> result = subTypeController.findById(id);

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
        when(subTypeService.findById(any()))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        Mono<ResponseEntity<SubTypeResponse>> result = subTypeController.findById(id);

        StepVerifier.create(result)
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR &&
                                response.getBody() == null
                )
                .verifyComplete();
    }
}
