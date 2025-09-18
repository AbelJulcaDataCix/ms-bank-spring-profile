package com.dataprogramming.profile.service.impl;

import com.dataprogramming.profile.entity.CustomerType;
import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.repository.TypeCustomerRepository;
import com.dataprogramming.profile.service.CustomerTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerTypeServiceImpl implements CustomerTypeService {

    private final TypeCustomerRepository typeCustomerRepository;

    private final SubTypeServiceImpl subTypeServiceImpl;

    @Override
    public Mono<CustomerType> create(CustomerType typeCustomer) {
        return typeCustomerRepository.save(typeCustomer);
    }

    @Override
    public Flux<CustomerType> findAll() {
        return typeCustomerRepository.findAll();
    }

    @Override
    public Mono<CustomerType> findById(String id) {
        return typeCustomerRepository.findById(id);
    }

    @Override
    public Mono<CustomerType> update(CustomerType typeCustomer) {
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
