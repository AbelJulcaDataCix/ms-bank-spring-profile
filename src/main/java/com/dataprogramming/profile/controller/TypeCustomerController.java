package com.dataprogramming.profile.controller;

import com.dataprogramming.profile.dto.CustomerTypeRequest;
import com.dataprogramming.profile.dto.CustomerTypeResponse;
import com.dataprogramming.profile.dto.CustomerTypeUpdateRequest;
import com.dataprogramming.profile.mapper.CustomerTypeMapper;
import com.dataprogramming.profile.service.CustomerTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RefreshScope
@RestController
@RequiredArgsConstructor
@RequestMapping("/typeCustomer")
public class TypeCustomerController {

    private final CustomerTypeService typeCustomerService;
    private final CustomerTypeMapper customerTypeMapper;

    @GetMapping("/list")
    public Flux<CustomerTypeResponse> list(){
        return typeCustomerService.findAll()
                .map(customerTypeMapper::toTypeCustomerResponse)
                .doOnSubscribe(subscription -> log.info("Starting to fetch all TypeCustomers"))
                .doOnComplete(() -> log.info("Completed fetching all TypeCustomers"))
                .doOnError(throwable -> log.error("Error occurred while fetching TypeCustomers:", throwable));
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<CustomerTypeResponse>> findById(@PathVariable String id){

        if (!StringUtils.hasText(id)) {
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }

        return typeCustomerService.findById(id)
                .map(customerTypeMapper::toTypeCustomerResponse)
                .map(subType -> new ResponseEntity<>(subType, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)))
                .doOnSuccess(response ->
                        log.info("Find operation successful with status: {}", response.getStatusCode()))
                .onErrorResume(throwable -> {
                    log.error("Error during find operation:", throwable);
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<CustomerTypeResponse>> create(@RequestBody CustomerTypeRequest typeCustomerRequest){
        return typeCustomerService.checkSubType(typeCustomerRequest.getSubType().getId())
                .flatMap(subType -> {
                    typeCustomerRequest.setSubType(subType);
                    return typeCustomerService.create(customerTypeMapper.toCustomerType(typeCustomerRequest))
                            .map(customerTypeMapper::toTypeCustomerResponse)
                            .map(tc -> new ResponseEntity<>(tc , HttpStatus.CREATED));
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<CustomerTypeResponse>> update(@Valid @RequestBody CustomerTypeUpdateRequest request) {
        return typeCustomerService.update(customerTypeMapper.toCustomerTypeUpdate(request))
                .map(customerTypeMapper::toTypeCustomerResponse)
                .map(savedCustomer -> new ResponseEntity<>(savedCustomer, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return typeCustomerService.delete(id)
                .flatMap(isDeleted -> {
                    if (Boolean.TRUE.equals(isDeleted)) {
                        return Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
                    } else {
                        log.warn("not find object for delete");
                        return Mono.just(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
                    }
                })
                .doOnSuccess(responseEntity ->
                        log.info("subType delete completed with status: {}", responseEntity.getStatusCode()))
                .onErrorResume(throwable -> {
                    log.error("Error during delete operation: ", throwable);
                    return Mono.just(new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
}
