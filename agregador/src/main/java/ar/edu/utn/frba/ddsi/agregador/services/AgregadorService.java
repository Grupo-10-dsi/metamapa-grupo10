package ar.edu.utn.frba.ddsi.agregador.services;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.ColeccionRepository;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.SolicitudesRepository;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Coleccion;


import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AgregadorService {
    private final HechosRepository hechosRepository;
    private final SolicitudesRepository solicitudesRepository;
    private final ColeccionRepository coleccionRepository;

    public AgregadorService(HechosRepository hechosRepository, SolicitudesRepository solicitudesRepository, ColeccionRepository coleccionRepository) {
        this.hechosRepository = hechosRepository;
        this.solicitudesRepository = solicitudesRepository;
        this.coleccionRepository = coleccionRepository;
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public List<Hecho> consultarHechosPeriodicamente() {
        return hechosRepository.importarHechosDesdeFuentes();
    }

    // TODO: Implementar métodos para crear, obtener, eliminar y modificar colecciones
    public Coleccion crearColeccion(Coleccion coleccion){
        if (!this.coleccionRepository.estaRepetida(coleccion){
            return this.coleccionRepository.createCollection(coleccion);
        }
    }

    public Coleccion obtenerColeccion(UUID id){ return this.coleccionRepository.findById();}

    public Coleccion eliminarColeccionPorId(UUID id) { return this.coleccionRepository.findAndDelete(id);}

    public Coleccion modificarColeccion(UUID id, Coleccion coleccion) { return this.coleccionRepository.findAndUpdate(id, coleccion);}



    public List<Hecho> encontrarHechosFiltrados(
            String categoria,
            String fechaReporteDesde,
            String fechaReporteHasta,
            String fechaAcontecimientoDesde,
            String fechaAcontecimientoHasta,
            Double latitud,
            Double longitud
    ) {
        //TODO: traerHechosFuentes()
        return hechosRepository.findAll().stream()
                .filter(hecho -> categoria == null ||
                        (hecho.getCategoria() != null && categoria.equalsIgnoreCase(hecho.getCategoria().getDetalle())))
                .filter(hecho -> {
                    if (fechaReporteDesde == null && fechaReporteHasta == null) return true;
                    if (hecho.getFechaCarga() == null) return false;
                    boolean desde = fechaReporteDesde == null ||
                            !hecho.getFechaCarga().isBefore(LocalDateTime.parse(fechaReporteDesde));
                    boolean hasta = fechaReporteHasta == null ||
                            !hecho.getFechaCarga().isAfter(LocalDateTime.parse(fechaReporteHasta));
                    return desde && hasta;
                })
                .filter(hecho -> {
                    if (fechaAcontecimientoDesde == null && fechaAcontecimientoHasta == null) return true;
                    if (hecho.getFechaAcontecimiento() == null) return false;
                    boolean desde = fechaAcontecimientoDesde == null ||
                            !hecho.getFechaAcontecimiento().isBefore(LocalDate.parse(fechaAcontecimientoDesde));
                    boolean hasta = fechaAcontecimientoHasta == null ||
                            !hecho.getFechaAcontecimiento().isAfter(LocalDate.parse(fechaAcontecimientoHasta));
                    return desde && hasta;
                })
                .filter(hecho -> {
                    if (latitud == null && longitud == null) return true;
                    if (hecho.getUbicacion() == null) return false;
                    boolean latOk = latitud == null || hecho.getUbicacion().getLatitud().equals(latitud);
                    boolean lonOk = longitud == null || hecho.getUbicacion().getLongitud().equals(longitud);
                    return latOk && lonOk;
                })
                .collect(Collectors.toList());
    }

    // TODO: Las solicitudes de eliminacion no se crean en el agregador, sino que se crean en las fuentes (dinamica y metamapa?)
    public List<SolicitudEliminacion> encontrarSolicitudes() {
        return solicitudesRepository.findAll();
    }

    public SolicitudEliminacion modificarEstadoSolicitud(UUID id, Estado_Solicitud nuevoEstado) {

        SolicitudEliminacion solicitudAEditar = solicitudesRepository.findById(id);

        if (solicitudAEditar == null) {
            throw new IllegalArgumentException("Solicitud no encontrada con ID: " + id);
        }

        solicitudAEditar.setEstado(nuevoEstado);

        SolicitudEliminacion solicitudActualizada = solicitudesRepository.findByIdAndUpdate(id, solicitudAEditar);

        if (solicitudActualizada == null) {
            throw new RuntimeException("No se pudo actualizar la solicitud con ID: " + id);
        }

        if(nuevoEstado == Estado_Solicitud.ACEPTADA) {
            this.ocultarHecho(solicitudAEditar.getIdHecho());
        }

        return solicitudActualizada;

    }

    private void ocultarHecho(UUID idHecho) {
        Hecho hechoParaOcultar = hechosRepository.findById(idHecho);

        if (hechoParaOcultar == null) {
            throw new IllegalArgumentException("Hecho no encontrado con ID: " + idHecho);
        }

        hechoParaOcultar.setEstaOculto(true);

        Hecho hechoActualizado = hechosRepository.findByIdAndUpdate(idHecho, hechoParaOcultar);

        if (hechoActualizado == null) {
            throw new RuntimeException("No se pudo ocultar el hecho con ID: " + idHecho);
        }
    }


    private void consenso(UUID idColeccion) {

    }
}



