package com.dataprogramming.profile.repository;

import com.dataprogramming.profile.entity.CustomerType;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TypeCustomerRepository extends ReactiveMongoRepository<CustomerType, String> {
}
