package ar.edu.utn.frba.ddsi.agregador.models.entities.conversor;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.EtiquetaDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoMultimediaDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoTextualDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.*;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.Estatica;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.origenFuente.OrigenFuente;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.CategoriaRepository;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.ContribuyenteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class Conversor {

    @Value("${normalizador.url}")
    private String normalizadorUrl;

    public Conversor() {}

    public Hecho convertirHecho(HechoDTO hechoDTO, OrigenFuente origen, ContribuyenteRepository contribuyenteRepository, CategoriaRepository categoriaRepository) {
        HechoDTO hechoNormalizado = this.aplicarNormalizacion(hechoDTO);

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
        WebClient webClient = WebClient.create(normalizadorUrl);

        Class<? extends HechoDTO> dtoClass;
        if (hecho instanceof HechoMultimediaDTO) {
            dtoClass = HechoMultimediaDTO.class;
        } else if (hecho instanceof HechoTextualDTO) {
            dtoClass = HechoTextualDTO.class;
        } else {
            dtoClass = HechoDTO.class;
        }

        return webClient.patch()
                .uri("/normalizador/normalizar")
                .bodyValue(hecho)
                .retrieve()
                .bodyToMono(dtoClass)
                .block();
    }


    public Hecho creacionHecho(HechoDTO hechoDTO, OrigenFuente origen, Categoria categoria) {

        if (hechoDTO instanceof HechoMultimediaDTO) {
            return crearHechoMultimediaBase((HechoMultimediaDTO) hechoDTO, origen, categoria);
        } else if (hechoDTO instanceof HechoTextualDTO) {
            return crearHechoTextualBase((HechoTextualDTO) hechoDTO, origen, categoria);
        } else {
            if (esHechoMultimedia(hechoDTO)) {
                return crearHechoMultimediaDesdeBase(hechoDTO, origen, categoria);
            } else {
                return crearHechoTextualDesdeBase(hechoDTO, origen, categoria);
            }
        }
    }

    private boolean esHechoMultimedia(HechoDTO hechoDTO) {
        return hechoDTO instanceof HechoMultimediaDTO ||
               (!(hechoDTO instanceof HechoTextualDTO) && hasMultimediaContent(hechoDTO));
    }

    private boolean hasMultimediaContent(HechoDTO hechoDTO) {
        try {
            java.lang.reflect.Method method = hechoDTO.getClass().getMethod("getContenidoMultimedia");
            Object result = method.invoke(hechoDTO);
            return result != null && result instanceof java.util.List && !((java.util.List<?>) result).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private Hecho crearHechoTextualDesdeBase(HechoDTO hechoDTO, OrigenFuente origen, Categoria categoria) {
        String cuerpo = null;
        try {
            java.lang.reflect.Method method = hechoDTO.getClass().getMethod("getCuerpo");
            Object result = method.invoke(hechoDTO);
            cuerpo = (String) result;
        } catch (Exception e) {
            // No cuerpo field available
        }

        return new HechoTextual(
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                categoria,
                hechoDTO.getUbicacion(),
                hechoDTO.getFechaAcontecimiento(),
                hechoDTO.getFechaCarga(),
                origen,
                convertirEtiquetas(hechoDTO.getEtiquetas()),
                null,
                cuerpo
        );
    }

    private Hecho crearHechoMultimediaDesdeBase(HechoDTO hechoDTO, OrigenFuente origen, Categoria categoria) {
        java.util.List<String> contenidoMultimedia = new java.util.ArrayList<>();
        try {
            java.lang.reflect.Method method = hechoDTO.getClass().getMethod("getContenidoMultimedia");
            Object result = method.invoke(hechoDTO);
            if (result instanceof java.util.List) {
                contenidoMultimedia = (java.util.List<String>) result;
            }
        } catch (Exception e) {
            // No contenidoMultimedia field available
        }

        return new HechoMultimedia(
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                categoria,
                hechoDTO.getUbicacion(),
                hechoDTO.getFechaAcontecimiento(),
                hechoDTO.getFechaCarga(),
                origen,
                convertirEtiquetas(hechoDTO.getEtiquetas()),
                null,
                contenidoMultimedia
        );
    }

    private Hecho crearHechoTextualBase(HechoTextualDTO hechoDTO, OrigenFuente origen, Categoria categoria) {
        Hecho hecho =  new HechoTextual(
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                categoria,
                hechoDTO.getUbicacion(),
                hechoDTO.getFechaAcontecimiento(),
                hechoDTO.getFechaCarga(),
                origen,
                convertirEtiquetas(hechoDTO.getEtiquetas()),
                null,
                hechoDTO.getCuerpo()
        );
        return hecho;
    }

    public Hecho crearHechoMultimediaBase(HechoMultimediaDTO hechoDTO, OrigenFuente origen, Categoria categoria) {
        Hecho hecho = new HechoMultimedia(
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                categoria,
                hechoDTO.getUbicacion(),
                hechoDTO.getFechaAcontecimiento(),
                hechoDTO.getFechaCarga(),
                origen,
                convertirEtiquetas(hechoDTO.getEtiquetas()),
                null,
                hechoDTO.getContenidoMultimedia()
        );
        return hecho;
    }

    private List<Etiqueta> convertirEtiquetas(List<EtiquetaDTO> etiquetasDTO) {
        if (etiquetasDTO == null) return new ArrayList<>();
        return etiquetasDTO.stream()
                .map(dto -> {
                    Etiqueta etiqueta = new Etiqueta();
                    etiqueta.setId(dto.getId());
                    etiqueta.setDescripcion(dto.getDescripcion());
                    return etiqueta;
                })
                .collect(java.util.stream.Collectors.toList());
    }
}

