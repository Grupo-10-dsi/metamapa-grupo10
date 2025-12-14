package ar.edu.utn.frba.ddsi.apigateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Optional;

@Configuration
public class RateLimiterConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        // Resuelve la IP del cliente para usarla como clave del límite
        return exchange -> Mono.just(
                Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"))
                        .map(ip -> ip.split(",")[0].trim()) // Si hay multiples proxies, toma la IP original
                        .orElseGet(() -> Optional.ofNullable(exchange.getRequest().getRemoteAddress())
                                .map(InetSocketAddress::getAddress)
                                .map(addr -> addr.getHostAddress())
                                .orElse("unknown") // Fallback seguro si no se puede determinar la IP
                        )
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