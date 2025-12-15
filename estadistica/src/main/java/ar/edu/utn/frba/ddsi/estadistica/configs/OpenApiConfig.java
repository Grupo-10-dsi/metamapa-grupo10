package ar.edu.utn.frba.ddsi.estadistica.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("https://metamapa.page/swagger-estadistica");
        server.setDescription("Servidor de Producción");

        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("API de Estadísticas - MetaMapa")
                        .version("1.0")
                        .description("Documentación de endpoints del módulo de estadísticas"));
    }
}