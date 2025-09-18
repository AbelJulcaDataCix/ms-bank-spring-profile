package com.dataprogramming.profile.dto;

import com.dataprogramming.profile.model.EnumSubType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubTypeUpdateRequest {
    @NotBlank
    @NotNull(message = "The 'value' field cannot be null")
    private String id;
    @NotNull(message = "The 'value' field cannot be null")
    private EnumSubType value;
}
