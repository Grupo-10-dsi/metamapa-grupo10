package ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.importador;


import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.adaptador.Adaptador;
import org.springframework.web.reactive.function.client.WebClient;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Importador {

    protected WebClient webClient;
    private final Adaptador adaptador = new Adaptador();
    private final List<String> urls;

    public Importador(List<String> urls) {
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8080/api/")
                .build();
        this.urls = urls;
    }

    // IDEA: HACER HECHODTO QUE TENGA !TODOS! LOS CAMPOS, Y DESPUES HACER UN ADAPTADOR QUE SI LE LLEGA CAMPO NULL PONGA UNA LISTA VACIA O CONTRIBUYENTE ANONIMO
    public List<Hecho> importarHechos() {
        List<Hecho> hechosImportados = new ArrayList<>();
        for(String url : urls) {
            List<HechoDTO> hechos = webClient.get() // Inicia construcción de un GET
                    .uri(url)// Usa la URI construida con filtros
                    .retrieve() // Realiza la solicitud (envía el GET)
                    .bodyToFlux(HechoDTO.class)// Convierte la respuesta JSON en un flujo de objetos `Hecho`
                    .collectList()// Junta el flujo en una lista
                    .block();

            if (hechos != null) {
                List<Hecho> hechosMapeados = hechos.stream().map(adaptador::convertirHecho).collect(Collectors.toList());
                hechosImportados.addAll(hechosMapeados);
            }
                // Si no hay (nuevos ?) hechos, sigue la misma lista?

        }
        return hechosImportados;
    }

}








