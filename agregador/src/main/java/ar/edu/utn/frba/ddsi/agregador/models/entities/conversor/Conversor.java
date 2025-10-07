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
import ar.edu.utn.frba.ddsi.agregador.models.repositories.ContribuyenteRepository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

public class Conversor {
    public Conversor() {}

    public Hecho convertirHecho(HechoDTO hechoDTO, OrigenFuente origen, ContribuyenteRepository contribuyenteRepository) {
        HechoDTO hechoNormalizado = this.aplicarNormalizacion(hechoDTO);
        //System.out.println("Convirtiendo hecho: " + hechoNormalizado.getTitulo() + " de la fuente: " + hechoDTO.getOrigenFuente());
        Hecho hecho = creacionHecho(hechoNormalizado, origen);
        // Caso fuente estática
        if (origen instanceof Estatica) {
            ((HechoTextual) hecho).setCuerpo(hechoDTO.getDescripcion());
            hecho.setContribuyente(Anonimo.getInstance());
            hecho.setEtiquetas(new ArrayList<>());
        }

        //hecho.setOrigenFuente(origen);

        hecho.setCantidadMenciones(1);
        hecho.setContribuyente(this.instanciarContribuyente(hechoDTO.getContribuyente(), contribuyenteRepository));
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
        Hecho hecho =  new HechoTextual(
                hechoDTO.getId(),
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                hechoDTO.getCategoria(),
                hechoDTO.getUbicacion(),
                hechoDTO.getFechaAcontecimiento(),
                hechoDTO.getFechaCarga(),
                origen,
                hechoDTO.getEtiquetas(),
                null,
                hechoDTO.getCuerpo()
        );
        return hecho;
    }

    public Hecho crearHechoMultimediaBase(HechoDTO hechoDTO, OrigenFuente origen) {
        Hecho hecho = new HechoMultimedia(
                hechoDTO.getId(),
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                hechoDTO.getCategoria(),
                hechoDTO.getUbicacion(),
                hechoDTO.getFechaAcontecimiento(),
                hechoDTO.getFechaCarga(),
                origen,
                hechoDTO.getEtiquetas(),
                null,
                hechoDTO.getContenidoMultimedia()
        );
        return hecho;
    }

    public Contribuyente instanciarContribuyente(Contribuyente contribuyenteHechoDTO, ContribuyenteRepository contribuyenteRepository) {
        if (contribuyenteHechoDTO instanceof Anonimo) {
            return contribuyenteRepository.findContribuyenteById(1);
        } else {
            Contribuyente nuevoRegistrado = new Registrado(contribuyenteHechoDTO.getNombre());
            contribuyenteRepository.saveAndFlush(nuevoRegistrado);
            return nuevoRegistrado;
        }
    }

}

