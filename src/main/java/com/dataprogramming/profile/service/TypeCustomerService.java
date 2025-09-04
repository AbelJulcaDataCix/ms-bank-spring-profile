package com.dataprogramming.profile.service;

import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.entity.TypeCustomer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TypeCustomerService {
    public Mono<TypeCustomer> create(TypeCustomer typeCustomer);

    public Flux<TypeCustomer> findAll();

    public Mono<TypeCustomer> findById(String id);

    public Mono<TypeCustomer> update(TypeCustomer typeCustomer);

    public Mono<Boolean> delete(String id);

    public Mono<SubType> checkSubType(String id);
}
