package com.dataprogramming.profile.entity;

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
public class TypeCustomer {
    @Id
    private String id;
    @Valid
    private EnumTypeCustomer value;

    private SubType subType;

    public enum EnumTypeCustomer {
        BUSINESS, PERSONAL
    }
}
