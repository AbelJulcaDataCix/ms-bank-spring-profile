package com.dataprogramming.profile.repository;

import com.dataprogramming.profile.entity.SubType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface SubTypeRepository extends ReactiveMongoRepository<SubType, String> {
}
