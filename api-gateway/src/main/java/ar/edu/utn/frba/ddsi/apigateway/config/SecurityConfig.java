package ar.edu.utn.frba.ddsi.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.cors(cors -> {})
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtSpec ->
                    jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter())
            ))

                // sin esto no puedo hacer post a los endpoints que requieren roles especificos
                .cors(cors -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", config);
                    cors.configurationSource(source);
                })


                // 2. Definir las reglas de autorización (BASADO EN TU CONTROLLER)
                .authorizeExchange(exchanges ->
                    exchanges

                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        // --- REGLAS PÚBLICAS (permitAll) ---
                        .pathMatchers(HttpMethod.GET, "/agregador/colecciones").permitAll()
                        .pathMatchers(HttpMethod.GET, "/agregador/colecciones/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/agregador/categorias").permitAll()
                        .pathMatchers(HttpMethod.GET, "/agregador/hechos").permitAll()
                        .pathMatchers(HttpMethod.GET, "/agregador/hechos/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/agregador/search").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/dinamica/hechos").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/dinamica/upload/{id}").permitAll()
                            .pathMatchers(HttpMethod.POST, "/agregador/solicitudes").permitAll()

                        // --- REGLAS DE "ADMIN" (hasRole) ---
                        .pathMatchers(HttpMethod.POST, "/agregador/colecciones").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PATCH, "/agregador/colecciones/{id}").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/agregador/colecciones/{id}").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/agregador/solicitudes").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/agregador/solicitudes/{id}").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/estadisticas/categorias").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/estadisticas/colecciones/provincia-max-hechos").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/estadisticas/hechos/max-categoria").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/estadisticas/categoria/provincia-max-hechos").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/estadisticas/categoria/hora").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.GET, "/api/estadisticas/solicitudes/spam").hasRole("ADMIN")

                        // --- REGLAS DE USUARIO AUTENTICADO (authenticated) ---
                        .pathMatchers(HttpMethod.POST, "/agregador/solicitudes").authenticated() // Un usuario logueado crea una solicitud
                        .pathMatchers("/perfil/**").authenticated()

                    // --- REGLA FINAL (Catch-All) ---
                    .anyExchange().authenticated()
                )

                .csrf(ServerHttpSecurity.CsrfSpec::disable); // Deshabilitar CSRF para APIs

        return http.build();
    }


    /**
     * Esta clase sirve para configurar los roles del JWT que me vienen de Keycloak ==> (Son distintos los nombres de los de Keycloak a los de Spring Security)
     */
    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter()); //
        return jwtAuthenticationConverter;
    }


    /**
     * Esta clase hace una conversion de los permisos de Keycloak a los que maneja Spring Security.
     */
    static class KeycloakRealmRoleConverter implements Converter<Jwt, Flux<GrantedAuthority>> {

        @Override
        @SuppressWarnings("unchecked")
        public Flux<GrantedAuthority> convert(Jwt jwt) {

            if (jwt.getClaims() == null || jwt.getClaims().get("realm_access") == null) {
                return Flux.empty();
            }

            Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
            List<String> roles = (List<String>) realmAccess.get("roles");

            if (roles == null || roles.isEmpty()) {
                return Flux.empty();
            }

            return Flux.fromIterable(roles)
                    .map(roleName -> "ROLE_" + roleName.toUpperCase())
                    .map(SimpleGrantedAuthority::new);
        }
    }
}