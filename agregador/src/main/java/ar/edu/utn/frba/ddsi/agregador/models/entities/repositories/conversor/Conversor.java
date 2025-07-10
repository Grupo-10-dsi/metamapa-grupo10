package ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.conversor;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.HechoMultimedia;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.HechoTextual;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Origen_Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Anonimo;

import java.util.List;

public class Conversor {
    public Conversor() {}

    public Hecho convertirHecho(HechoDTO hechoDTO) {
        Hecho hecho = creacionHecho(hechoDTO);
        // Caso fuente estática
        if (hecho.getOrigenFuente() == Origen_Fuente.ESTATICA) {
            ((HechoTextual) hecho).setCuerpo(hechoDTO.getDescripcion());
            hecho.setContribuyente(Anonimo.getInstance());
            hecho.setEtiquetas(List.of());
        }

        return hecho;
    }

    public Hecho creacionHecho(HechoDTO hechoDTO) {

        if (hechoDTO.getContenidoMultimedia() != null) {
            return crearHechoMultimediaBase(hechoDTO);
        } else {
            return crearHechoTextualBase(hechoDTO);
        }
    }

    private Hecho crearHechoTextualBase(HechoDTO hechoDTO) {
        return new HechoTextual(
                hechoDTO.getId(),
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                hechoDTO.getCategoria(),
                hechoDTO.getUbicacion(),
                hechoDTO.getFechaAcontecimiento(),
                hechoDTO.getFechaCarga(),
                hechoDTO.getOrigenFuente(),
                hechoDTO.getEtiquetas(),
                hechoDTO.getContribuyente(),
                hechoDTO.getCuerpo()
        );
    }

    public Hecho crearHechoMultimediaBase(HechoDTO hechoDTO) {
        return new HechoMultimedia(
                hechoDTO.getId(),
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                hechoDTO.getCategoria(),
                hechoDTO.getUbicacion(),
                hechoDTO.getFechaAcontecimiento(),
                hechoDTO.getFechaCarga(),
                hechoDTO.getOrigenFuente(),
                hechoDTO.getEtiquetas(),
                hechoDTO.getContribuyente(),
                hechoDTO.getContenidoMultimedia()
        );
    }

}

