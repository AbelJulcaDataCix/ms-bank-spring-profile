package com.dataprogramming.profile.entity;

import com.dataprogramming.profile.model.EnumCustomerType;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document("TypeCustomer")
@AllArgsConstructor
@NoArgsConstructor
public class CustomerType {
    @Id
    private String id;
    @Valid
    private EnumCustomerType value;

    private SubType subType;

}
