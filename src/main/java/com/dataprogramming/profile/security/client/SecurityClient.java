package com.dataprogramming.profile.security.client;

import com.dataprogramming.profile.security.domain.TokenResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Mono;


@HttpExchange(url = "http://localhost:8010/auth")
public interface SecurityClient {

    @HttpExchange(method = "POST", url = "/validate")
    Mono<TokenResponse> validateToken(@RequestHeader("Authorization") String token);

}
