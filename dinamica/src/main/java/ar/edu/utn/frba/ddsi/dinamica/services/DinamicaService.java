package ar.edu.utn.frba.ddsi.dinamica.services;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.ContribuyenteDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.HechoMultimedia;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.HechoTextual;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.hecho.Contribuyente;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.personas.Anonimo;

import ar.edu.utn.frba.ddsi.dinamica.models.repositories.*;
import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.Estado_Solicitud;


import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import ar.edu.utn.frba.ddsi.dinamica.models.entities.solicitudEliminacion.SolicitudEliminacion;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DinamicaService {
    private final HechosRepository hechosRepository;
    private final SolicitudesRepository solicitudesRepository;
    private final ContribuyenteRepository contribuyenteRepository;
    private final CategoriaRepository categoriaRepository;
    private final ImagenesRepository imagenesRepository;

    public DinamicaService(HechosRepository hechosRepository, SolicitudesRepository solicitudesRepository, ContribuyenteRepository contribuyenteRepository, CategoriaRepository categoriaRepository, ImagenesRepository imagenesRepository) {
        this.hechosRepository = hechosRepository;
        this.solicitudesRepository = solicitudesRepository;
        this.contribuyenteRepository = contribuyenteRepository;
        this.categoriaRepository = categoriaRepository;
        this.imagenesRepository = imagenesRepository;
    }

    // <---------------------------------- CREACION DE HECHOS ---------------------------------->

    public Integer crearHecho(HechoDTO hechoDTO) {

        Hecho hecho = this.hechoFromDTO(hechoDTO);


        return hechosRepository.save(hecho).getId();
    }


//    @PostConstruct
//    private void inicializarContribuyente() {
//        Contribuyente anonimoExistente = contribuyenteRepository.findById(1).orElse(null);
//
//        if (anonimoExistente == null) {
//            // Crear e insertar el anónimo con ID manual
//            Anonimo anonimo = Anonimo.getInstance();
//            contribuyenteRepository.saveAndFlush(anonimo);
//        }
//    }


    private Contribuyente determinarContribuyente(ContribuyenteDTO contribuyenteDTO) {
//        if (hechoDTO.getRegistrado() == null) {
//            return contribuyenteRepository.findById(1).orElse(null);
//        } else {
//            Contribuyente nuevoRegistrado = new Registrado(hechoDTO.getRegistrado().getNombre());
//            contribuyenteRepository.saveAndFlush(nuevoRegistrado);
//            return nuevoRegistrado;
//        }
        if(contribuyenteDTO == null) {
            return new Contribuyente(0, "Anonimo");
        } else {
            return contribuyenteRepository.findByCloakId(contribuyenteDTO);
        }

    }

    private Hecho hechoFromDTO (HechoDTO hechoDTO) {
        if (hechoDTO.getTipo().equalsIgnoreCase("textual")) {
            return new HechoTextual(
                    hechoDTO.getTitulo(),
                    hechoDTO.getDescripcion(),
                    determinarCategoria(hechoDTO.getCategoria().getDetalle()),
                    hechoDTO.getUbicacion(),
                    hechoDTO.getFechaAcontecimiento(),
                    hechoDTO.getEtiquetas(),
                    determinarContribuyente(hechoDTO.getContribuyente()),
                    hechoDTO.getCuerpo()
            );
        } else if (hechoDTO.getTipo().equalsIgnoreCase("multimedia")) {
            return new HechoMultimedia(
                    hechoDTO.getTitulo(),
                    hechoDTO.getDescripcion(),
                    determinarCategoria(hechoDTO.getCategoria().getDetalle()),
                    hechoDTO.getUbicacion(),
                    hechoDTO.getFechaAcontecimiento(),
                    hechoDTO.getEtiquetas(),
                    determinarContribuyente(hechoDTO.getContribuyente()),
                    hechoDTO.getContenidoMultimedia()
            );
        } else {
            throw new IllegalArgumentException("Tipo de hecho no soportado: " + hechoDTO.getTipo());
        }
    }

    private Categoria determinarCategoria(String detalle) {
        Categoria categoria = categoriaRepository.findCategoriaByDetalle(detalle);
        if (categoria == null) {
            categoria = categoriaRepository.saveAndFlush(new Categoria(detalle));
        }
        return categoria;
    }


    // <---------------------------------- ACTUALIZACION DE HECHOS ---------------------------------->

    public Hecho actualizarHecho(Integer id, HechoDTO hechoDTO) {


        Hecho hechoAEditar = hechosRepository.findById(id).orElse(null);

        if (hechoAEditar == null) {
            throw new IllegalArgumentException("Hecho no encontrado con ID: " + id);
        }

        if (!hechoAEditar.esEditable()) {
            throw new RuntimeException("El hecho no es editable");
        }

        Hecho nuevoHecho = hechoFromDTO(hechoDTO);
        nuevoHecho.setId(id);

        return hechosRepository.save(nuevoHecho);
    }

    public void guardarImagen(Integer idHecho, MultipartFile file) {
        HechoMultimedia hecho = (HechoMultimedia) hechosRepository.findById(idHecho).orElse(null);
        if (hecho == null) {
            throw new IllegalArgumentException("Hecho no encontrado con ID: " + idHecho);
        }

        String nombreArchivo = imagenesRepository.cargarImagenSupabase(file);

        hecho.addContenidoMultimedia(nombreArchivo);

        hechosRepository.save(hecho);

    }

    // <---------------------------------- GESTION DE SOLICITUDES DE ELIMINACION ---------------------------------->

    public Integer crearSolicitudEliminacion(SolicitudDTO solicitudDTO) {

        SolicitudEliminacion nuevaSolicitudEliminacion = new SolicitudEliminacion();

        nuevaSolicitudEliminacion.setJustificacion(solicitudDTO.getJustificacion());

        if(DetectorDeSpam.esSpam(nuevaSolicitudEliminacion.getJustificacion())) {
            nuevaSolicitudEliminacion.setEstado(Estado_Solicitud.RECHAZADA);
        }

//        if (!nuevaSolicitudEliminacion.esCorrecta()) {
//            throw new IllegalArgumentException("La justificación debe tener al menos 500 caracteres.");
//        }

        // Verifico si el hecho existe
        Hecho hechoAeliminar = hechosRepository.findById(solicitudDTO.getIdHecho()).orElse(null);

        if (hechoAeliminar == null) {
            throw new IllegalArgumentException("Hecho no encontrado con ID: " + solicitudDTO.getIdHecho());
        }

        nuevaSolicitudEliminacion.setHecho(hechoAeliminar);

        solicitudesRepository.save(nuevaSolicitudEliminacion);

        return nuevaSolicitudEliminacion.getId();
    }

    public SolicitudEliminacion modificarEstadoSolicitud(Integer id, Estado_Solicitud nuevoEstado) {

        SolicitudEliminacion solicitudAEditar = solicitudesRepository.findById(id).orElse(null);

        if (solicitudAEditar == null) {
            throw new IllegalArgumentException("Solicitud no encontrada con ID: " + id);
        }

        solicitudAEditar.setEstado(nuevoEstado);

        SolicitudEliminacion solicitudActualizada = solicitudesRepository.save(solicitudAEditar);


//        if(nuevoEstado == Estado_Solicitud.ACEPTADA) {
//            this.ocultarHecho(solicitudAEditar.getIdHecho());
//        }

        return solicitudActualizada;

    }

//    private void ocultarHecho(Integer idHecho) {
//        Hecho hechoParaOcultar = hechosRepository.findById(idHecho).orElse(null);
//
//        if (hechoParaOcultar == null) {
//            throw new IllegalArgumentException("Hecho no encontrado con ID: " + idHecho);
//        }
//
//        //hechoParaOcultar.setEstaOculto(true);
//
//        hechosRepository.save(hechoParaOcultar);
//
////        if (hechoActualizado == null) {
////            throw new RuntimeException("No se pudo ocultar el hecho con ID: " + idHecho);
////        }
//    }




    public List<Hecho> encontrarHechosFiltrados(
            String ultimaConsulta,
            String categoria,
            String fechaReporteDesde,
            String fechaReporteHasta,
            String fechaAcontecimientoDesde,
            String fechaAcontecimientoHasta,
            Double latitud,
            Double longitud
    )
    {
        System.out.println("Consulta desde dinamica:" + ultimaConsulta);
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
                .filter(hecho -> {
                    if (ultimaConsulta == null) return true;
                    //System.out.println(ultimaConsulta);
                    return hecho.getFechaCarga().isAfter(LocalDateTime.parse(ultimaConsulta));
                })
                .collect(Collectors.toList());
    }

    public List<SolicitudEliminacion> encontrarSolicitudes() {
        return solicitudesRepository.findAll();
    }
}





