package ar.edu.utn.frba.ddsi.agregador.services;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Algoritmo_Consenso;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios.*;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.ColeccionDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.CriterioDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Filtro;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.HechosRepository;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.ColeccionRepository;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.SolicitudesRepository;
import ar.edu.utn.frba.ddsi.agregador.models.entities.repositories.clasificador.Clasificador;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Coleccion;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;


import java.time.LocalDate;
import java.util.List;
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


    public void consultarHechosPorPrimeraVez() {
        hechosRepository.importarHechosDesdeFuentes(); // Se conecta a las otras API's y pone los hechos en instancias de las fuentes
        this.clasificarHechos();
    }

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void consultarHechosPeriodicamente() {
        hechosRepository.importarHechosDesdeFuentes(); // Se conecta a las otras API's y pone los hechos en instancias de las fuentes
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void clasificarHechos() {

        List<Fuente> fuentes = hechosRepository.findAllFuentes();

        for (Fuente fuente : fuentes) {
            List<Fuente> fuentesParaEvaluar = fuentes.stream()
                    .filter(f -> !f.equals(fuente))  // excluye la fuente actual de la iteración
                    .collect(Collectors.toList());


            List<Hecho> hechosClasificados = Clasificador.clasificarHechosPorMenciones(fuente.getHechos(), fuentesParaEvaluar);
            fuente.setHechos(hechosClasificados);
        }

        hechosRepository.update(fuentes);
    }

    // <----------------- COLECCIONES ----------------->
    // TODO: Implementar manejo de errores y validaciones

    public UUID crearColeccion(ColeccionDTO coleccionDTO){

        List<Fuente> fuentes = this.hechosRepository.findFuentes(coleccionDTO.getUrls_fuente());

        if (fuentes == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND ,"Fuente no encontrada, URL: " + coleccionDTO.getUrls_fuente());
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
            case "fechaAcontecimientoDesde" -> new CriterioFechaDesde(LocalDateTime.parse(criterioDTO.getValor()));
            case "fechaAcontecimientoHasta" -> new CriterioFechaHasta(LocalDateTime.parse(criterioDTO.getValor()));
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

    public List<Hecho> obtenerHechosCurados(UUID id) {
        Coleccion coleccion = this.coleccionRepository.findById(id);
        if (coleccion == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con ID: " + id);
        }
        return coleccion.getFuentes().stream()
                .flatMap(fuente -> fuente.getHechos().stream())
                .filter(hecho -> hecho.consensuado(this.mencionesNecesarias(coleccion.getAlgoritmo_consenso())))
                .collect(Collectors.toList());
    }

    private Double mencionesNecesarias(Algoritmo_Consenso algoritmo) {
        return switch (algoritmo) {
            case MULTIPLES_MENCIONES -> 2.0;
            case MAYORIA_SIMPLE -> Math.floor(this.hechosRepository.countFuentes() / 2.0);
            case ABSOLUTA -> Math.ceil(this.hechosRepository.countFuentes());
            default -> 0.0; // Algoritmo de consenso = 'NINGUNO'
        };
    }

    public Coleccion obtenerColeccion(UUID id){ return this.coleccionRepository.findById(id);}

    public Coleccion eliminarColeccionPorId(UUID id) { return this.coleccionRepository.findAndDelete(id);}

    public Coleccion actualizarColeccion(UUID id, ColeccionDTO coleccionDTO) {

        List<CriterioPertenencia> criterios = coleccionDTO.getCriterios().stream()
                .map(this::criterioFromDTO)
                .toList();

        List<Fuente> fuentes = this.hechosRepository.findFuentes(coleccionDTO.getUrls_fuente());


        Coleccion coleccionEditada = new Coleccion(
                coleccionDTO.getTitulo(),
                coleccionDTO.getDescripcion(),
                coleccionDTO.getAlgoritmo_consenso(),
                fuentes,
                criterios
        );

        return this.coleccionRepository.findByIdAndUpdate(id, coleccionEditada);
    }

    public Coleccion modificarAlgoritmoConsenso(UUID id, Algoritmo_Consenso nuevoAlgoritmo){
        Coleccion coleccionAModificadar = this.coleccionRepository.findById(id);
        if (coleccionAModificadar == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con ID: " + id);
        }
        coleccionAModificadar.setAlgoritmo_consenso(nuevoAlgoritmo);
        return this.coleccionRepository.findByIdAndUpdate(id, coleccionAModificadar);
    }

    public Coleccion modificarListaDeFuentes(UUID id, List<String> urls_fuente) {
        Coleccion coleccionAModificadar = this.coleccionRepository.findById(id);
        if (coleccionAModificadar == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con ID: " + id);
        }

        List<Fuente> fuentes = this.hechosRepository.findFuentes(urls_fuente);
        
        coleccionAModificadar.setFuentes(fuentes);

        return this.coleccionRepository.findByIdAndUpdate(id, coleccionAModificadar);

    }

    public List<Hecho> encontrarHechosPorColeccion(
            UUID id,
            Filtro filtros
    ) {


        Coleccion coleccion = this.coleccionRepository.findById(id);

        if (coleccion == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con ID: " + id);
        }

        List<Hecho> hechos = coleccion.mostrarHechos();

        return this.hechosFiltrados(hechos, filtros);


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

    public SolicitudEliminacion crearSolicitudEliminacion(SolicitudDTO solicitudDTO) {
            SolicitudEliminacion nuevaSolicitud = new SolicitudEliminacion(
                    solicitudDTO.getIdHecho(),
                    solicitudDTO.getJustificacion()
            );

            this.solicitudesRepository.save(nuevaSolicitud);

        return nuevaSolicitud;
    }

    public List<Hecho> hechosFiltrados(List<Hecho> hechos, Filtro filtros) {
        String categoria = filtros.getCategoria();
        String fechaReporteDesde = filtros.getFecha_reporte_desde();
        String fechaReporteHasta = filtros.getFecha_reporte_hasta();
        String fechaAcontecimientoDesde = filtros.getFecha_acontecimiento_desde();
        String fechaAcontecimientoHasta = filtros.getFecha_acontecimiento_hasta();
        Double latitud = filtros.getLatitud();
        Double longitud = filtros.getLongitud();

        return hechos.stream()
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

}



