package com.dataprogramming.profile.repository;

import com.dataprogramming.profile.entity.TypeCustomer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TypeCustomerRepository extends ReactiveMongoRepository<TypeCustomer, String> {
}
