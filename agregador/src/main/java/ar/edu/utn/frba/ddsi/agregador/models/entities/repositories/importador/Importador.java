package ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.importador;


import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.conversor.Conversor;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.List;

public class Importador {

    protected WebClient webClient;
    private final Conversor adaptador = new Conversor();

    public Importador() {
        this.webClient = WebClient.builder()
                .baseUrl("")
                .build();
    }

    // IDEA: HACER HECHODTO QUE TENGA !TODOS! LOS CAMPOS, Y DESPUES HACER UN ADAPTADOR QUE SI LE LLEGA CAMPO NULL PONGA UNA LISTA VACIA O CONTRIBUYENTE ANONIMO
    public void importarHechos(Fuente fuente) {

        List<HechoDTO> hechos = webClient.get() // Inicia construcción de un GET
                .uri(fuente.getUrl())// Usa la URI construida con filtros
                .retrieve() // Realiza la solicitud (envía el GET)
                .bodyToFlux(HechoDTO.class)// Convierte la respuesta JSON en un flujo de objetos `Hecho`
                .collectList()// Junta el flujo en una lista
                .block();

        if (hechos != null) {
            fuente.setHechos(hechos.stream().map(adaptador::convertirHecho).toList());

        }
    }

}








