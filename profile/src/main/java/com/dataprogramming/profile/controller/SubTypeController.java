package com.dataprogramming.profile.controller;

import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.service.SubTypeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RefreshScope
@RestController
@RequestMapping("/subtype")
public class SubTypeController {

    @Autowired
    private SubTypeService subTypeService;

    @PostMapping("/create")
    public Mono<ResponseEntity<SubType>> create(@Valid @RequestBody SubType subType) {
        return subTypeService.create(subType)
                .map(savedSubType -> new ResponseEntity<>(savedSubType, HttpStatus.CREATED))
                .doOnSuccess(subTypeResponse -> log.info("SubType create successful"))
                .doOnError(throwable -> log.error("error: ", throwable));
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<SubType>> update(@Valid @RequestBody SubType subType) {
        return subTypeService.findById(subType.getId())
                .flatMap(existingSubType -> {
                    existingSubType.setValue(subType.getValue());
                    return subTypeService.update(existingSubType)
                            .map(updatedSubType -> new ResponseEntity<>(updatedSubType, HttpStatus.OK));
                })
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)))
                .doOnSuccess(response -> {
                    log.info("Update operation complete. Status: {}", response.getStatusCode());
                })
                .doOnError(throwable -> {
                    log.error("Error during update operation: ", throwable);
                });
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return subTypeService.delete(id)
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

    @GetMapping("/list")
    public Flux<SubType> list() {
        return subTypeService.findAll()
                .doOnSubscribe(subscription -> log.info("Subscription to subType list received"))
                .doOnTerminate(() -> log.info("List operation complete"));
    }

    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<SubType>> findById(@PathVariable String id) {

        if (!StringUtils.hasText(id)) {
            return Mono.just(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
        }

        return subTypeService.findById(id)
                .map(subType -> new ResponseEntity<>(subType, HttpStatus.OK))
                .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)))
                .doOnSuccess(response -> log.info("Find operation successful with status: {}", response.getStatusCode()))
                .onErrorResume(throwable -> {
                    log.error("Error during find operation:", throwable);
                    return Mono.just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
                });
    }
}
