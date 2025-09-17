package com.dataprogramming.profile.mapper;

import com.dataprogramming.profile.dto.CustomerTypeRequest;
import com.dataprogramming.profile.dto.CustomerTypeResponse;
import com.dataprogramming.profile.dto.CustomerTypeUpdateRequest;
import com.dataprogramming.profile.entity.CustomerType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerTypeMapper {

    CustomerTypeResponse toTypeCustomerResponse(CustomerType typeCustomer);

    @Mapping(target = "id", ignore = true)
    CustomerType toCustomerType(CustomerTypeRequest customerTypeRequest);

    CustomerType toCustomerTypeUpdate(CustomerTypeUpdateRequest customerTypeUpdateRequest);

}
