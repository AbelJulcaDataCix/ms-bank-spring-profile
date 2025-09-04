package com.dataprogramming.profile.service.impl;

import com.dataprogramming.profile.entity.SubType;
import com.dataprogramming.profile.repository.SubTypeRepository;
import com.dataprogramming.profile.service.SubTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SubTypeServiceImpl implements SubTypeService {

    @Autowired
    private SubTypeRepository subTypeRepository;

    @Override
    public Mono<SubType> create(SubType subType) {
        return subTypeRepository.save(subType);
    }

    @Override
    public Flux<SubType> findAll() {
        return subTypeRepository.findAll();
    }

    @Override
    public Mono<SubType> findById(String id) {
        return subTypeRepository.findById(id);
    }

    @Override
    public Mono<SubType> update(SubType profile) {
        return subTypeRepository.save(profile);
    }

    @Override
    public Mono<Boolean> delete(String id) {
        return subTypeRepository.findById(id)
                .flatMap(delete ->
                        subTypeRepository.delete(delete).then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }
}
