package ar.edu.utn.frba.ddsi.agregador.config;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoMultimediaDTOGraph;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoTextualDTOGraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLTypeResolverConfig {

    @Bean
    public RuntimeWiringConfigurer typeResolverConfigurer() {
        return wiringBuilder -> wiringBuilder
                .type("Hecho", typeWiring -> typeWiring.typeResolver(env -> {
                    Object src = env.getObject();
                    if (src instanceof HechoTextualDTOGraph) {
                        return env.getSchema().getObjectType("HechoTextual");
                    }
                    if (src instanceof HechoMultimediaDTOGraph) {
                        return env.getSchema().getObjectType("HechoMultimedia");
                    }
                    throw new IllegalStateException(
                            "Tipo Hecho no soportado: " + src.getClass()
                    );
                }));
    }
}