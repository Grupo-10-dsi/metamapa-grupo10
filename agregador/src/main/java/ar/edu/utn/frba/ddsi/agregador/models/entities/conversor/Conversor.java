package ar.edu.utn.frba.ddsi.agregador.models.entities.conversor;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.HechoMultimedia;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.HechoTextual;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.Estatica;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Anonimo;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Registrado;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

public class Conversor {
    public Conversor() {}

    public Hecho convertirHecho(HechoDTO hechoDTO, OrigenFuente origen) {
        HechoDTO hechoNormalizado = this.aplicarNormalizacion(hechoDTO);
        Hecho hecho = creacionHecho(hechoNormalizado, origen);
        // Caso fuente estática
        if (origen instanceof Estatica) {
            ((HechoTextual) hecho).setCuerpo(hechoDTO.getDescripcion());
            hecho.setContribuyente(Anonimo.getInstance());
            hecho.setEtiquetas(new ArrayList<>());
        }

        hecho.setCantidadMenciones(1);

        return hecho;
    }

    public HechoDTO aplicarNormalizacion(HechoDTO hecho) {
        WebClient webClient = WebClient.create("http://localhost:8087");
        return webClient.patch()
                .uri("/normalizador/normalizar")
                .bodyValue(hecho)
                .retrieve()
                .bodyToMono(HechoDTO.class)
                .block();
    }


    public Hecho creacionHecho(HechoDTO hechoDTO, OrigenFuente origen) {

        if (hechoDTO.getContenidoMultimedia() != null) {
            return crearHechoMultimediaBase(hechoDTO, origen);
        } else {
            return crearHechoTextualBase(hechoDTO, origen);
        }
    }

    private Hecho crearHechoTextualBase(HechoDTO hechoDTO, OrigenFuente origen) {
        return new HechoTextual(
                hechoDTO.getId(),
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                hechoDTO.getCategoria(),
                hechoDTO.getUbicacion(),
                hechoDTO.getFechaAcontecimiento(),
                hechoDTO.getFechaCarga(),
                origen,
                hechoDTO.getEtiquetas(),
                this.instanciarContribuyente(hechoDTO.getContribuyente()),
                hechoDTO.getCuerpo()
        );
    }

    public Hecho crearHechoMultimediaBase(HechoDTO hechoDTO, OrigenFuente origen) {
        return new HechoMultimedia(
                hechoDTO.getId(),
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                hechoDTO.getCategoria(),
                hechoDTO.getUbicacion(),
                hechoDTO.getFechaAcontecimiento(),
                hechoDTO.getFechaCarga(),
                origen,
                hechoDTO.getEtiquetas(),
                this.instanciarContribuyente(hechoDTO.getContribuyente()),
                hechoDTO.getContenidoMultimedia()
        );
    }

    public Contribuyente instanciarContribuyente(Contribuyente contribuyenteHechoDTO) {
        if (contribuyenteHechoDTO == null ) {
            return Anonimo.getInstance();
        } else {
            return new Registrado(contribuyenteHechoDTO.getNombre());
        }
    }

}

