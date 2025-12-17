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
        return exchange -> Mono.just(
                Objects.requireNonNull(exchange.getRequest().getRemoteAddress())
                        .getAddress().getHostAddress()
        );
    }

    // @Bean
    // public KeyResolver userKeyResolver() {
    //     return exchange -> ReactiveSecurityContextHolder.getContext()
    //         .map(ctx -> ctx.getAuthentication().getName())
    //         .defaultIfEmpty("anonymous");
    // }
}