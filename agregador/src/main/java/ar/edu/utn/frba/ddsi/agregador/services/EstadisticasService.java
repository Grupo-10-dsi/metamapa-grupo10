package ar.edu.utn.frba.ddsi.agregador.services;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.SolicitudDTOE;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.UbicacionDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.EstadisticasRepository;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.SolicitudesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {
    private final EstadisticasRepository estadisticasRepository;
    private final SolicitudesRepository solicitudesRepository;

    public EstadisticasService(EstadisticasRepository estadisticasRepository, SolicitudesRepository solicitudesRepository) {
        this.estadisticasRepository = estadisticasRepository;
        this.solicitudesRepository = solicitudesRepository;
    }

    // SERVICIOS PARA EL MÓDULO DE ESTADÍSTICAS
    public List<Ubicacion> obtenerUbicacionesColeccion(Integer idColeccion) {
        return this.estadisticasRepository.obtenerUbicacionesColeccion(idColeccion);
    }

    public List<Categoria> obtenerCategoriasConMasHechos(Integer cantidadCategorias) {
        return this.estadisticasRepository.obtenerCategoriasConMasHechos(cantidadCategorias);
    }

//    public Ubicacion obtenerUbicacionMasFrecuenteDeColeccion(Integer idColeccion) {
//        return this.estadisticasRepository.obtenerUbicacionMasFrecuenteDeColeccion(idColeccion);
//    }
//
    public List<Ubicacion> obtenerUbicacionesCategoria(Integer idCategoria) {
        return this.estadisticasRepository.obtenerUbicacionesCategoria(idCategoria);
    }

    public List<LocalTime> obtenerHorasMasFrecuente(Integer Id, Integer cantidadHoras) {
        List<Integer> horas =  this.estadisticasRepository.obtenerHorasMasFrecuente(Id, cantidadHoras);
        return horas.stream()
                .map(h -> LocalTime.of(h, 0))
                .collect(Collectors.toList());
    }

    public List<SolicitudDTOE> obtenerSolicitudesSpam() {
        List<SolicitudEliminacion> solicitudes = this.solicitudesRepository.findAllByEstado(Estado_Solicitud.SPAM);

        return solicitudes.stream().map(SolicitudEliminacion::toDTO).toList();
    }
}
