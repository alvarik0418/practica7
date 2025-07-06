package org.example.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class LedgerRouterHandler {

    @Bean
    public RouterFunction<ServerResponse> router(){
        return RouterFunctions.route(POST("/ledger/entries"),this::createEntry);
    }

    private Mono<ServerResponse> createEntry(ServerRequest request) {
        return request.bodyToMono(Map.class)
                .flatMap(body -> {
                    Map<String, Object> result = new HashMap<>();
                    result.put("status", "POSTED");
                    result.put("id", UUID.randomUUID().toString());
                    result.put("createdAt", Instant.now());

                    return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .bodyValue(result);
                });
    }
}
