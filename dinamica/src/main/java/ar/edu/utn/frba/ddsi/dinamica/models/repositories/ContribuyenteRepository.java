package ar.edu.utn.frba.ddsi.dinamica.models.repositories;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.ContribuyenteDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Contribuyente;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ContribuyenteRepository {


    private WebClient webClient;

    @Value("${gestor-personas.url.url}/internal/usuarios")
    private String baseUrl;

    public ContribuyenteRepository() {
        this.webClient = WebClient.builder()
                .build();

    }

    public Contribuyente findByCloakId(ContribuyenteDTO contribuyente) {
        return webClient.post()
                .uri(baseUrl + "/find-or-create")
                .bodyValue(contribuyente)
                .retrieve()
                .bodyToMono(Contribuyente.class)
                .block();
    }



}
