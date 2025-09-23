package com.dataprogramming.profile.config;

import com.dataprogramming.profile.security.client.SecurityClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    SecurityClient securityClient(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory =
                HttpServiceProxyFactory
                        .builderFor(WebClientAdapter.create(webClient))
                        .build();
        return httpServiceProxyFactory.createClient(SecurityClient.class);
    }

}
