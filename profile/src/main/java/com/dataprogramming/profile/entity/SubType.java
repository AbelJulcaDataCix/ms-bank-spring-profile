package com.dataprogramming.profile.entity;

import jakarta.validation.constraints.NotNull;
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
@Document("SubType")
@AllArgsConstructor
@NoArgsConstructor
public class SubType {

    @Id
    private String id;
    @NotNull(message = "The 'value' field cannot be null")
    private EnumSubType value;

    public enum EnumSubType{
        NORMAL, VIP, PYME
    }
}
