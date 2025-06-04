package ar.edu.utn.frba.ddsi.dinamica.services;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoMultimediaDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoTextualDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.HechoMultimedia;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.HechoTextual;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.repositories.DinamicaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DinamicaService {
    private final DinamicaRepository dinamicaRepository;

    public DinamicaService(DinamicaRepository dinamicaRepository) {
        this.dinamicaRepository = dinamicaRepository;
    }

    public void crearHechoTextual(HechoTextualDTO hechoDTO) {
        HechoTextual hecho = new HechoTextual(
            hechoDTO.getTitulo(),
            hechoDTO.getDescripcion(),
            hechoDTO.getCategoria(),
            hechoDTO.getUbicacion(),
            hechoDTO.getFechaAcontecimiento(),
            LocalDateTime.now(),
            hechoDTO.getEtiquetas(),
            hechoDTO.getContribuyente(),
            hechoDTO.getContribuyente().esAnonimo(),
            hechoDTO.getCuerpo()
        );

        dinamicaRepository.save(hecho);
    }

    public void crearHechoMultimedia(HechoMultimediaDTO hechoDTO) {

        HechoMultimedia hecho = new HechoMultimedia(
            hechoDTO.getTitulo(),
            hechoDTO.getDescripcion(),
            hechoDTO.getCategoria(),
            hechoDTO.getUbicacion(),
            hechoDTO.getFechaAcontecimiento(),
            LocalDateTime.now(),
            hechoDTO.getEtiquetas(),
            hechoDTO.getContribuyente(),
            hechoDTO.getContenidoMultimedia(),
            hechoDTO.getContribuyente().esAnonimo()
        );

        dinamicaRepository.save(hecho);
    }
}
