package ar.edu.utn.frba.ddsi.agregador.services;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios.*;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.ColeccionDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.CriterioDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
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
import java.util.UUID;

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
    public void consultarHechosPeriodicamente() {
        hechosRepository.importarHechosDesdeFuentes();
    }

    // <----------------- COLECCIONES ----------------->
    // TODO: Implementar manejo de errores y validaciones

    public UUID crearColeccion(ColeccionDTO coleccionDTO){
//        if (!coleccionRepository.estaRepetida(coleccion){
//            return this.coleccionRepository.save(coleccion);
//        }
        List<Fuente> fuentes = this.hechosRepository.findFuentes(coleccionDTO.getUrls_fuente());

        if (fuentes == null) {
            throw new IllegalArgumentException("Fuente no encontrada, URL: " + coleccionDTO.getUrls_fuente());
        }

        List<CriterioPertenencia> criterios = coleccionDTO.getCriterios().stream()
                .map(this::criterioFromDTO)
                .toList();

        Coleccion nuevaColeccion = new Coleccion(
                coleccionDTO.getTitulo(),
                coleccionDTO.getDescripcion(),
                coleccionDTO.getAlgoritmo_consenso(),
                fuentes,
                criterios
        );

        this.coleccionRepository.save(nuevaColeccion);

        return nuevaColeccion.getId();
    }

    public CriterioPertenencia criterioFromDTO(CriterioDTO criterioDTO) {
        return switch (criterioDTO.getTipo()) {
            case "titulo" -> new CriterioTitulo(criterioDTO.getValor());
            case "descripcion" -> new CriterioDescripcion(criterioDTO.getValor());
            case "categoria" -> new CriterioCategoria(new Categoria(criterioDTO.getValor()));
            case "fechaAcontecimientoDesde" -> new CriterioFechaDesde(LocalDate.parse(criterioDTO.getValor()));
            case "fechaAcontecimientoHasta" -> new CriterioFechaHasta(LocalDate.parse(criterioDTO.getValor()));
            case "ubicacion" -> //chequear
                    new CriterioUbicacion(new Ubicacion(
                            Double.parseDouble(criterioDTO.getValor().split(",")[0]),
                            Double.parseDouble(criterioDTO.getValor().split(",")[1])
                    ));
            default -> throw new IllegalArgumentException("Tipo de criterio desconocido: " + criterioDTO.getTipo());
        };
    }

    public List<Coleccion> obtenerColecciones() {
        return this.coleccionRepository.findAll();
    }

    public Coleccion obtenerColeccion(UUID id){ return this.coleccionRepository.findById(id);}

    public Coleccion eliminarColeccionPorId(UUID id) { return this.coleccionRepository.findAndDelete(id);}

//    public Coleccion modificarColeccion(UUID id, ColeccionDTO coleccionDTO) {
//
//        Coleccion coleccionEditada = new Coleccion(
//                coleccionDTO.getTitulo(),
//                coleccionDTO.getDescripcion(),
//                coleccionDTO.getHechos(),
//                coleccionDTO.getAlgoritmo_consenso()
//        );
//
//        return this.coleccionRepository.findByIdAndUpdate(id, coleccionEditada);
//    }

    public List<Hecho> encontrarHechosPorColeccion(UUID idColeccion) {
        Coleccion coleccion = this.coleccionRepository.findById(idColeccion);

        return coleccion.mostrarHechos();
    }


//    public List<Hecho> encontrarHechosFiltrados(
//            String categoria,
//            String fechaReporteDesde,
//            String fechaReporteHasta,
//            String fechaAcontecimientoDesde,
//            String fechaAcontecimientoHasta,
//            Double latitud,
//            Double longitud
//    ) {
//        //TODO: traerHechosFuentes()
//        return hechosRepository.findAll().stream()
//                .filter(hecho -> categoria == null ||
//                        (hecho.getCategoria() != null && categoria.equalsIgnoreCase(hecho.getCategoria().getDetalle())))
//                .filter(hecho -> {
//                    if (fechaReporteDesde == null && fechaReporteHasta == null) return true;
//                    if (hecho.getFechaCarga() == null) return false;
//                    boolean desde = fechaReporteDesde == null ||
//                            !hecho.getFechaCarga().isBefore(LocalDateTime.parse(fechaReporteDesde));
//                    boolean hasta = fechaReporteHasta == null ||
//                            !hecho.getFechaCarga().isAfter(LocalDateTime.parse(fechaReporteHasta));
//                    return desde && hasta;
//                })
//                .filter(hecho -> {
//                    if (fechaAcontecimientoDesde == null && fechaAcontecimientoHasta == null) return true;
//                    if (hecho.getFechaAcontecimiento() == null) return false;
//                    boolean desde = fechaAcontecimientoDesde == null ||
//                            !hecho.getFechaAcontecimiento().isBefore(LocalDate.parse(fechaAcontecimientoDesde));
//                    boolean hasta = fechaAcontecimientoHasta == null ||
//                            !hecho.getFechaAcontecimiento().isAfter(LocalDate.parse(fechaAcontecimientoHasta));
//                    return desde && hasta;
//                })
//                .filter(hecho -> {
//                    if (latitud == null && longitud == null) return true;
//                    if (hecho.getUbicacion() == null) return false;
//                    boolean latOk = latitud == null || hecho.getUbicacion().getLatitud().equals(latitud);
//                    boolean lonOk = longitud == null || hecho.getUbicacion().getLongitud().equals(longitud);
//                    return latOk && lonOk;
//                })
//                .collect(Collectors.toList());
//    }




    private void consenso(UUID idColeccion) {

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

        return solicitudActualizada;

    }
}



