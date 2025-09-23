package com.dataprogramming.profile.security.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenData {
    private String token;
    private String username;
    private String role;
    private boolean enabled;
}
