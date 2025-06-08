package ar.edu.utn.frba.ddsi.dinamica.services;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoMultimediaDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoTextualDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.HechoMultimedia;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.HechoTextual;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.Estado_Solicitud;
import org.springframework.stereotype.Service;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.repositories.SolicitudesRepository;

import java.util.UUID;

@Service
public class DinamicaService {
    private final HechosRepository hechosRepository;
    private final SolicitudesRepository solicitudesRepository;

    public DinamicaService(HechosRepository hechosRepository, SolicitudesRepository solicitudesRepository) {
        this.hechosRepository = hechosRepository;
        this.solicitudesRepository = solicitudesRepository;
    }

    // <---------------------------------- CREACION DE HECHOS ---------------------------------->

    public UUID crearHecho(Object hechoDTO) {
        if (hechoDTO instanceof HechoTextualDTO) {
            return crearHechoTextual((HechoTextualDTO) hechoDTO);
        } else if (hechoDTO instanceof HechoMultimediaDTO) {
            return crearHechoMultimedia((HechoMultimediaDTO) hechoDTO);
        } else {
            throw new IllegalArgumentException("Tipo de hecho no soportado");
        }
    }


    private UUID crearHechoTextual(HechoTextualDTO hechoDTO) {
        HechoTextual hecho = new HechoTextual(
            hechoDTO.getTitulo(),
            hechoDTO.getDescripcion(),
            hechoDTO.getCategoria(),
            hechoDTO.getUbicacion(),
            hechoDTO.getFechaAcontecimiento(),
            hechoDTO.getEtiquetas(),
            hechoDTO.getContribuyente(),
            hechoDTO.getCuerpo()
        );

        hechosRepository.save(hecho);

        return hecho.getId();
    }

    private UUID crearHechoMultimedia(HechoMultimediaDTO hechoDTO) {

        HechoMultimedia hecho = new HechoMultimedia(
            hechoDTO.getTitulo(),
            hechoDTO.getDescripcion(),
            hechoDTO.getCategoria(),
            hechoDTO.getUbicacion(),
            hechoDTO.getFechaAcontecimiento(),
            hechoDTO.getEtiquetas(),
            hechoDTO.getContribuyente(),
            hechoDTO.getContenidoMultimedia()
        );

        hechosRepository.save(hecho);

        return hecho.getId();
    }

    // <---------------------------------- ACTUALIZACION DE HECHOS ---------------------------------->

    public void actualizarHecho(UUID id, Object hechoDTO) {
        if (hechoDTO instanceof HechoTextualDTO) {
            actualizarHechoTextual(id, (HechoTextualDTO) hechoDTO);
        } else if (hechoDTO instanceof HechoMultimediaDTO) {
            actualizarHechoMultimedia(id, (HechoMultimediaDTO) hechoDTO);
        } else {
            throw new IllegalArgumentException("Tipo de hecho no soportado");
        }
    }


    private void actualizarHechoTextual(UUID id, HechoTextualDTO hechoDTO) {
            Hecho hechoAEditar = hechosRepository.findById(id);

            if (hechoAEditar == null) {
                throw new IllegalArgumentException("Hecho no encontrado con ID: " + id);
            }


            HechoTextual hechoTextualEditar = (HechoTextual) hechoAEditar;


            if (hechoTextualEditar.esEditable()) {
                hechoTextualEditar.setTitulo(hechoDTO.getTitulo());
                hechoTextualEditar.setDescripcion(hechoDTO.getDescripcion());
                hechoTextualEditar.setCategoria(hechoDTO.getCategoria());
                hechoTextualEditar.setUbicacion(hechoDTO.getUbicacion());
                hechoTextualEditar.setFechaAcontecimiento(hechoDTO.getFechaAcontecimiento());
                hechoTextualEditar.setEtiquetas(hechoDTO.getEtiquetas());
                hechoTextualEditar.setContribuyente(hechoDTO.getContribuyente());
                hechoTextualEditar.setCuerpo(hechoDTO.getCuerpo());

                hechosRepository.save(hechoTextualEditar);
            } else {
                throw new RuntimeException("El hecho no es editable");
            }
    }

    private void actualizarHechoMultimedia(UUID id, HechoMultimediaDTO hechoDTO) {
        Hecho hechoAEditar = hechosRepository.findById(id);

        if (hechoAEditar == null) {
            throw new IllegalArgumentException("Hecho no encontrado con ID: " + id);
        }


        HechoMultimedia hechoMultimediaEditar = (HechoMultimedia) hechoAEditar;


        if (hechoMultimediaEditar.esEditable()) {
            hechoMultimediaEditar.setTitulo(hechoDTO.getTitulo());
            hechoMultimediaEditar.setDescripcion(hechoDTO.getDescripcion());
            hechoMultimediaEditar.setCategoria(hechoDTO.getCategoria());
            hechoMultimediaEditar.setUbicacion(hechoDTO.getUbicacion());
            hechoMultimediaEditar.setFechaAcontecimiento(hechoDTO.getFechaAcontecimiento());
            hechoMultimediaEditar.setEtiquetas(hechoDTO.getEtiquetas());
            hechoMultimediaEditar.setContribuyente(hechoDTO.getContribuyente());
            hechoMultimediaEditar.setContenidoMultimedia(hechoDTO.getContenidoMultimedia());

            hechosRepository.save(hechoMultimediaEditar);
        } else {
            throw new RuntimeException("El hecho no es editable");
        }
    }

    public UUID crearSolicitudEliminacion(SolicitudDTO solicitud) {

        SolicitudEliminacion nuevaSolicitudEliminacion = new SolicitudEliminacion(
            solicitud.getId(),
            solicitud.getJustificacion()
        );

        if(DetectorDeSpam.esSpam(nuevaSolicitudEliminacion.getJustificacion())) {
            nuevaSolicitudEliminacion.setEstado(Estado_Solicitud.RECHAZADA);
        }

        if (!nuevaSolicitudEliminacion.esCorrecta()) {
            throw new IllegalArgumentException("La justificación debe tener al menos 500 caracteres.");
        }

        // Verifico si el hecho existe
        Hecho hechoAeliminar = hechosRepository.findById(nuevaSolicitudEliminacion.getIdHecho());

        if (hechoAeliminar == null) {
            throw new IllegalArgumentException("Hecho no encontrado con ID: " + nuevaSolicitudEliminacion.getIdHecho());
        }

        solicitudesRepository.save(nuevaSolicitudEliminacion);

        return nuevaSolicitudEliminacion.getId();
    }

    public void modificarEstadoSolicitud(UUID id, Estado_Solicitud nuevoEstado) {

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
}





