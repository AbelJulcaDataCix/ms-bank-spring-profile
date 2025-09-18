package com.dataprogramming.profile.mapper;

import com.dataprogramming.profile.dto.SubTypeRequest;
import com.dataprogramming.profile.dto.SubTypeResponse;
import com.dataprogramming.profile.entity.SubType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubTypeMapper {

    @Mapping(target = "id", ignore = true)
    SubType toSubType(SubTypeRequest subTypeRequest);

    SubTypeResponse toSubTypeResponse(SubType subType);
}
