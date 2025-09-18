package com.dataprogramming.profile.service;

import com.dataprogramming.profile.entity.CustomerType;
import com.dataprogramming.profile.entity.SubType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerTypeService {
    public Mono<CustomerType> create(CustomerType typeCustomer);

    public Flux<CustomerType> findAll();

    public Mono<CustomerType> findById(String id);

    public Mono<CustomerType> update(CustomerType typeCustomer);

    public Mono<Boolean> delete(String id);

    public Mono<SubType> checkSubType(String id);
}
