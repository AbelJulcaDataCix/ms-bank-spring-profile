package com.dataprogramming.profile.dto;

import com.dataprogramming.profile.entity.SubType;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTypeUpdateRequest {
    private String id;
    private String value;
    private SubType subType;
}
