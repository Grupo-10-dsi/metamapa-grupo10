package ar.edu.utn.frba.ddsi.agregador.models.entities.conversor;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.*;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.Estatica;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.CategoriaRepository;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.ContribuyenteRepository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

public class Conversor {
    public Conversor() {}

    public Hecho convertirHecho(HechoDTO hechoDTO, OrigenFuente origen, ContribuyenteRepository contribuyenteRepository, CategoriaRepository categoriaRepository) {
        HechoDTO hechoNormalizado = this.aplicarNormalizacion(hechoDTO);
        //System.out.println("Convirtiendo hecho: " + hechoNormalizado.getTitulo() + " de la fuente: " + hechoDTO.getOrigenFuente());

        Categoria categoriaNormalizada = categoriaRepository.findCategoriaByDetalle(hechoNormalizado.getCategoria().getDetalle());
        if (categoriaNormalizada == null) {
            categoriaNormalizada = categoriaRepository.saveAndFlush(hechoNormalizado.getCategoria());
        }

        Contribuyente anonimo = new Contribuyente(0, "Anonimo");
        Hecho hecho = creacionHecho(hechoNormalizado, origen, categoriaNormalizada);
        hecho.setCantidadMenciones(1);

        // Caso fuente estática
        if (origen instanceof Estatica) {
            ((HechoTextual) hecho).setCuerpo(hechoDTO.getDescripcion());
            hecho.setContribuyente(anonimo);
            hecho.setEtiquetas(new ArrayList<>());
            return hecho;
        } else if (hechoDTO.getOrigenFuente() == Origen_Fuente_VIEJO.INTERMEDIARIA) {
            hecho.setContribuyente(anonimo);
            return hecho;

        }

        //hecho.setOrigenFuente(origen);


        hecho.setContribuyente(new Contribuyente(hechoDTO.getContribuyente().getContribuyente_id(), hechoDTO.getContribuyente().getContribuyente_nombre()));
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


    public Hecho creacionHecho(HechoDTO hechoDTO, OrigenFuente origen, Categoria categoria) {

        if (hechoDTO.getContenidoMultimedia() != null) {
            return crearHechoMultimediaBase(hechoDTO, origen, categoria);
        } else {
            return crearHechoTextualBase(hechoDTO, origen, categoria);
        }
    }

    private Hecho crearHechoTextualBase(HechoDTO hechoDTO, OrigenFuente origen, Categoria categoria) {
        Hecho hecho =  new HechoTextual(
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                categoria,
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

    public Hecho crearHechoMultimediaBase(HechoDTO hechoDTO, OrigenFuente origen, Categoria categoria) {
        Hecho hecho = new HechoMultimedia(
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                categoria,
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

//    public Contribuyente instanciarContribuyente(Contribuyente contribuyenteHechoDTO, ContribuyenteRepository contribuyenteRepository) {
//        if (contribuyenteHechoDTO instanceof Anonimo) {
//            return contribuyenteRepository.findContribuyenteById(1);
//        } else {
//            Contribuyente nuevoRegistrado = new Registrado(contribuyenteHechoDTO.getNombre());
//            contribuyenteRepository.saveAndFlush(nuevoRegistrado);
//            return nuevoRegistrado;
//        }
//    }

}

