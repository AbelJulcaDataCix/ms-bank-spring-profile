package com.dataprogramming.profile.service;

import com.dataprogramming.profile.entity.SubType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SubTypeService {
    public Mono<SubType> create(SubType subType);

    public Flux<SubType> findAll();

    public Mono<SubType> findById(String id);

    public Mono<SubType> update(SubType profile);

    public Mono<Boolean> delete(String id);
}
