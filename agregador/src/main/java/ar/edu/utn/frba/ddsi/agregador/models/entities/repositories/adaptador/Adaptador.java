package ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.adaptador;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Anonimo;

import java.util.List;

public class Adaptador {
    public Adaptador() {}

    public Hecho convertirHecho(HechoDTO hechoDTO) {
        Hecho hecho = new Hecho(
                hechoDTO.getId(),
                hechoDTO.getTitulo(),
                hechoDTO.getDescripcion(),
                hechoDTO.getCategoria(),
                hechoDTO.getUbicacion(),
                hechoDTO.getFechaAcontecimiento(),
                hechoDTO.getFechaCarga(),
                hechoDTO.getOrigenFuente(),
                hechoDTO.getEtiquetas(),
                hechoDTO.getContribuyente()
        );
        if (hecho.getContribuyente() == null) {
            hecho.setContribuyente(Anonimo.getInstance());
        }
        if (hecho.getEtiquetas() == null) {
            hecho.setEtiquetas(List.of());
        }
        return hecho;
    }
}