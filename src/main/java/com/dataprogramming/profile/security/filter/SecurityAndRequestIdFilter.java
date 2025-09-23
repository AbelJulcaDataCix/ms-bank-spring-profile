package com.dataprogramming.profile.security.filter;

import com.dataprogramming.profile.security.client.SecurityClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityAndRequestIdFilter implements WebFilter, Ordered {

    private final SecurityClient securityClient;
    public static final String REQUEST_ID_HEADER = "request-id";

    /**
     * Specifies the order of execution for this filter.
     * By returning Ordered.HIGHEST_PRECEDENCE, this filter is set to execute before other filters.
     *
     * @return the order value indicating the precedence of this filter
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * This filter performs two main functions:
     * 1. It verifies the presence and validity of the authorization token in the "Authorization" header.
     * If the token is invalid or not present, it responds with a 401 Unauthorized status.
     * 2. It handles the "request-id" header:
     * - If the "request-id" header is present in the request, it uses it.
     * - If it is not present, it generates a new UUID and assigns it as the "request-id".
     * - It ensures that the "request-id" header is always present in the response.
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String requestId = exchange.getRequest().getHeaders().getFirst(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isEmpty()) {
            requestId = java.util.UUID.randomUUID().toString();
            log.info("Generated new requestId: {}", requestId);
        } else {
            log.info("Using existing requestId from header: {}", requestId);
        }
        final String finalRequestId = requestId;

        exchange.getResponse().beforeCommit(() -> {
            exchange.getResponse().getHeaders().add(REQUEST_ID_HEADER, finalRequestId);
            log.info("Added header 'request-id' to response with value: {}", finalRequestId);
            return Mono.empty();
        });

        String authorizationHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("Authorization header not found or invalid format. Denying access.");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return securityClient.validateToken(authorizationHeader)
                .flatMap(response -> {
                    log.info("Token validated successfully. {}", response.getMessage());
                    return chain.filter(exchange)
                            .contextWrite(Context.of(REQUEST_ID_HEADER, finalRequestId));
                })
                .onErrorResume(throwable -> {
                    log.error("Failed to validate token: {}", throwable.getMessage());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }
}