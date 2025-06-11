package ar.edu.utn.frba.ddsi.dinamica.services;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoMultimediaDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoTextualDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.HechoMultimedia;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.HechoTextual;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.Estado_Solicitud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.repositories.SolicitudesRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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

    public Hecho crearHecho(Object hechoDTO) {
        HechoTextualDTO hechoTextualDTO = verificarYConvertirHechoDTO(hechoDTO, HechoTextualDTO.class);
        if (hechoTextualDTO != null) {
            return crearHechoTextual(hechoTextualDTO);
        }

        HechoMultimediaDTO hechoMultimediaDTO = verificarYConvertirHechoDTO(hechoDTO, HechoMultimediaDTO.class);
        if (hechoMultimediaDTO != null) {
            return crearHechoMultimedia(hechoMultimediaDTO);
        }

        throw new IllegalArgumentException("Tipo de hecho no soportado");
    }

    private <T> T verificarYConvertirHechoDTO(Object hechoDTO, Class<T> tipo) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            // Convertir el objeto a un mapa
            Map<String, Object> hechoMap = mapper.convertValue(hechoDTO, Map.class);

            // Verificar campos comunes obligatorios
            String[] camposObligatorios = {"titulo", "descripcion", "categoria", "ubicacion",
                    "fechaAcontecimiento", "contribuyente", "etiquetas"};

            for (String campo : camposObligatorios) {
                if (hechoMap.get(campo) == null) {
                    throw new IllegalArgumentException("Campo obligatorio faltante: " + campo);
                }
            }

            // Verificar tipo específico y validar contra el tipo esperado
            if (hechoMap.containsKey("cuerpo")) {
                if (tipo == HechoTextualDTO.class) {
                    return mapper.convertValue(hechoDTO, tipo);
                } else {
                    return null; // No es del tipo solicitado
                }
            } else if (hechoMap.containsKey("contenidoMultimedia")) {
                if (tipo == HechoMultimediaDTO.class) {
                    return mapper.convertValue(hechoDTO, tipo);
                } else {
                    return null; // No es del tipo solicitado
                }
            } else {
                throw new IllegalArgumentException("Tipo de hecho no identificado: falta campo 'cuerpo' o 'contenidoMultimedia'");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al procesar el DTO: " + e.getMessage());
        }
    }



    private Hecho crearHechoTextual(HechoTextualDTO hechoDTO) {
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

        return hecho;
    }

    private Hecho crearHechoMultimedia(HechoMultimediaDTO hechoDTO) {

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

        return hecho;
    }

    // <---------------------------------- ACTUALIZACION DE HECHOS ---------------------------------->

    public void actualizarHecho(UUID id, Object hechoDTO) {
        HechoTextualDTO hechoTextualDTO = verificarYConvertirHechoDTO(hechoDTO, HechoTextualDTO.class);
        if (hechoTextualDTO != null) {
            actualizarHechoTextual(id, hechoTextualDTO);
        }

        HechoMultimediaDTO hechoMultimediaDTO = verificarYConvertirHechoDTO(hechoDTO, HechoMultimediaDTO.class);
        if (hechoMultimediaDTO != null) {
            actualizarHechoMultimedia(id, hechoMultimediaDTO);
        }

        if(hechoTextualDTO == null && hechoMultimediaDTO == null) {
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

                hechosRepository.findByIdAndUpdate(id, hechoTextualEditar);
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

            hechosRepository.findByIdAndUpdate(id, hechoMultimediaEditar);
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





