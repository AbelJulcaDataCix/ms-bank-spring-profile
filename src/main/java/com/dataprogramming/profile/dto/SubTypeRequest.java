package com.dataprogramming.profile.dto;

import com.dataprogramming.profile.model.EnumSubType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubTypeRequest {

    @NotNull(message = "The 'value' field cannot be null")
    private EnumSubType value;
}
