package com.dataprogramming.profile.service.impl;

import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.entity.TypeCustomer;
import com.dataprogramming.profile.repository.TypeCustomerRepository;
import com.dataprogramming.profile.service.TypeCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TypeCustomerServiceImpl implements TypeCustomerService {

    @Autowired
    TypeCustomerRepository typeCustomerRepository;

    @Autowired
    SubTypeServiceImpl subTypeServiceImpl;

    @Override
    public Mono<TypeCustomer> create(TypeCustomer typeCustomer) {
        return typeCustomerRepository.save(typeCustomer);
    }

    @Override
    public Flux<TypeCustomer> findAll() {
        return typeCustomerRepository.findAll();
    }

    @Override
    public Mono<TypeCustomer> findById(String id) {
        return typeCustomerRepository.findById(id);
    }

    @Override
    public Mono<TypeCustomer> update(TypeCustomer typeCustomer) {
        return typeCustomerRepository.save(typeCustomer);
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return typeCustomerRepository.findById(id)
                .flatMap(
                        deleteTypeCustomer -> typeCustomerRepository.delete(deleteTypeCustomer)
                                .then(Mono.just(Boolean.TRUE))
                )
                .defaultIfEmpty(Boolean.FALSE);
    }

    @Override
    public Mono<SubType> checkSubType(String id) {
        return subTypeServiceImpl.findById(id);
    }
}
