package ar.edu.utn.frba.ddsi.dinamica.services;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.HechoMultimedia;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.HechoTextual;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Anonimo;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Contribuyente;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Registrado;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.Estado_Solicitud;


import org.springframework.stereotype.Service;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.repositories.SolicitudesRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DinamicaService {
    private final HechosRepository hechosRepository;
    private final SolicitudesRepository solicitudesRepository;

    public DinamicaService(HechosRepository hechosRepository, SolicitudesRepository solicitudesRepository) {
        this.hechosRepository = hechosRepository;
        this.solicitudesRepository = solicitudesRepository;
    }

    // <---------------------------------- CREACION DE HECHOS ---------------------------------->

    public UUID crearHecho(HechoDTO hechoDTO) {

        Hecho hecho = this.hechoFromDTO(hechoDTO);

        hechosRepository.save(hecho);

        return hecho.getId();
    }


    private Contribuyente determinarContribuyente(HechoDTO hechoDTO) {
        if (hechoDTO.getRegistrado() != null) {
            return new Registrado(
                hechoDTO.getRegistrado().getNombre(),
                hechoDTO.getRegistrado().getEmail(),
                hechoDTO.getRegistrado().getEdad()
            );
        } else {
            return Anonimo.getInstance();
        }
    }

    private Hecho hechoFromDTO (HechoDTO hechoDTO) {
        if (hechoDTO.getTipo().equalsIgnoreCase("textual")) {
            return new HechoTextual(
                    hechoDTO.getTitulo(),
                    hechoDTO.getDescripcion(),
                    hechoDTO.getCategoria(),
                    hechoDTO.getUbicacion(),
                    hechoDTO.getFechaAcontecimiento(),
                    hechoDTO.getEtiquetas(),
                    determinarContribuyente(hechoDTO),
                    hechoDTO.getCuerpo()
            );
        } else if (hechoDTO.getTipo().equalsIgnoreCase("multimedia")) {
            return new HechoMultimedia(
                    hechoDTO.getTitulo(),
                    hechoDTO.getDescripcion(),
                    hechoDTO.getCategoria(),
                    hechoDTO.getUbicacion(),
                    hechoDTO.getFechaAcontecimiento(),
                    hechoDTO.getEtiquetas(),
                    determinarContribuyente(hechoDTO),
                    hechoDTO.getContenidoMultimedia()
            );
        } else {
            throw new IllegalArgumentException("Tipo de hecho no soportado: " + hechoDTO.getTipo());
        }
    }


    // <---------------------------------- ACTUALIZACION DE HECHOS ---------------------------------->

    public Hecho actualizarHecho(UUID id, HechoDTO hechoDTO) {
        Hecho hechoAEditar = hechosRepository.findById(id);

        if (hechoAEditar == null) {
            throw new IllegalArgumentException("Hecho no encontrado con ID: " + id);
        }

        if (!hechoAEditar.esEditable()) {
            throw new RuntimeException("El hecho no es editable");
        }

        Hecho nuevoHecho = hechoFromDTO(hechoDTO);
        nuevoHecho.setId(id);

        return hechosRepository.findByIdAndUpdate(id, nuevoHecho);
    }

    // <---------------------------------- GESTION DE SOLICITUDES DE ELIMINACION ---------------------------------->

    public UUID crearSolicitudEliminacion(SolicitudDTO solicitud) {

        SolicitudEliminacion nuevaSolicitudEliminacion = new SolicitudEliminacion(
            solicitud.getIdHecho(),
            solicitud.getJustificacion()
        );

        if(DetectorDeSpam.esSpam(nuevaSolicitudEliminacion.getJustificacion())) {
            nuevaSolicitudEliminacion.setEstado(Estado_Solicitud.RECHAZADA);
        }

//        if (!nuevaSolicitudEliminacion.esCorrecta()) {
//            throw new IllegalArgumentException("La justificación debe tener al menos 500 caracteres.");
//        }

        // Verifico si el hecho existe
        Hecho hechoAeliminar = hechosRepository.findById(nuevaSolicitudEliminacion.getIdHecho());

        if (hechoAeliminar == null) {
            throw new IllegalArgumentException("Hecho no encontrado con ID: " + nuevaSolicitudEliminacion.getIdHecho());
        }

        solicitudesRepository.save(nuevaSolicitudEliminacion);

        return nuevaSolicitudEliminacion.getId();
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


    public List<Hecho> encontrarHechosFiltrados(
            String categoria,
            String fechaReporteDesde,
            String fechaReporteHasta,
            String fechaAcontecimientoDesde,
            String fechaAcontecimientoHasta,
            Double latitud,
            Double longitud
    ) {
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
                            !hecho.getFechaAcontecimiento().isBefore(LocalDateTime.parse(fechaAcontecimientoDesde));
                    boolean hasta = fechaAcontecimientoHasta == null ||
                            !hecho.getFechaAcontecimiento().isAfter(LocalDateTime.parse(fechaAcontecimientoHasta));
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

    public List<SolicitudEliminacion> encontrarSolicitudes() {
        return solicitudesRepository.findAll();
    }
}





