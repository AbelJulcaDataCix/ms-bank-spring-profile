package com.dataprogramming.profile.controller;

import com.dataprogramming.profile.entity.TypeCustomer;
import com.dataprogramming.profile.service.TypeCustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/typeCustomer")
public class TypeCustomerController {

    @Autowired
    private TypeCustomerService typeCustomerService;

    @GetMapping("/list")
    public Flux<TypeCustomer> list(){
        return typeCustomerService.findAll();
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<TypeCustomer>> findById(@PathVariable String id){

        if (!StringUtils.hasText(id)) {
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }

        return typeCustomerService.findById(id)
                .map(subType -> new ResponseEntity<>(subType, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)))
                .doOnSuccess(response -> log.info("Find operation successful with status: {}", response.getStatusCode()))
                .onErrorResume(throwable -> {
                    log.error("Error during find operation:", throwable);
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<TypeCustomer>> create(@RequestBody TypeCustomer typeCustomer){
        return typeCustomerService.checkSubType(typeCustomer.getSubType().getId())
                .flatMap(subType -> {
                    System.out.println("subType : " + subType.getValue());
                    typeCustomer.setSubType(subType);
                    System.out.println(typeCustomer);
                    return typeCustomerService.create(typeCustomer)
                            .map(tc -> new ResponseEntity<>(tc , HttpStatus.CREATED));
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<TypeCustomer>> update(@Valid @RequestBody TypeCustomer typeCustomer) {
        return typeCustomerService.update(typeCustomer)
                .map(savedCustomer -> new ResponseEntity<>(savedCustomer, HttpStatus.CREATED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return typeCustomerService.delete(id)
                .flatMap(isDeleted -> {
                    if (isDeleted) {
                        return Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
                    } else {
                        log.warn("not find object for delete");
                        return Mono.just(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
                    }
                })
                .doOnSuccess(responseEntity -> log.info("subType delete completed with status: {}", responseEntity.getStatusCode()))
                .onErrorResume(throwable -> {
                    log.error("Error during delete operation: ", throwable);
                    return Mono.just(new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
}
