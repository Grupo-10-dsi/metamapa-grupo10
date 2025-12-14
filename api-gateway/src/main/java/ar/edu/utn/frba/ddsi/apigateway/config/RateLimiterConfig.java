package ar.edu.utn.frba.ddsi.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import java.util.Objects;

@Configuration
public class RateLimiterConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        // Resuelve la IP del cliente para usarla como clave del límite
        return exchange -> Mono.just(
                Objects.requireNonNull(exchange.getRequest().getRemoteAddress())
                        .getAddress().getHostAddress()
        );
    }

    // OPCIONAL: Limitar por usuario logueado (JWT)
    // @Bean
    // public KeyResolver userKeyResolver() {
    //     return exchange -> ReactiveSecurityContextHolder.getContext()
    //         .map(ctx -> ctx.getAuthentication().getName())
    //         .defaultIfEmpty("anonymous");
    // }
}