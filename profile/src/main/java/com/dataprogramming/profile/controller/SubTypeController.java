package com.dataprogramming.profile.controller;

import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.service.SubTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RefreshScope
@RestController
@RequestMapping("/subtype")
public class SubTypeController {

    @Autowired
    private SubTypeService subTypeService;

    @PostMapping("/create")
    public Mono<ResponseEntity<SubType>> create(@RequestBody SubType subType){
        return subTypeService.create(subType)
                .map(savedCustomer -> new ResponseEntity<>(savedCustomer , HttpStatus.CREATED))
                .onErrorResume(t->Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

}
