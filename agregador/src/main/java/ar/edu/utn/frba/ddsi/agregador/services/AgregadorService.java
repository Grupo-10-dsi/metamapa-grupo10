package ar.edu.utn.frba.ddsi.agregador.services;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Algoritmo_Consenso;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.FuenteEstatica;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios.*;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.ColeccionDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.CriterioDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.SolicitudDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Filtro;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.importador.Importador;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Anonimo;
import ar.edu.utn.frba.ddsi.agregador.models.entities.personas.Contribuyente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.*;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ar.edu.utn.frba.ddsi.agregador.models.entities.clasificador.Clasificador;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Coleccion;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AgregadorService {
    private final HechosRepository hechosRepository;
    private final FuentesRepository fuentesRepository;
    private final SolicitudesRepository solicitudesRepository;
    private final ColeccionRepository coleccionRepository;
    private final ContribuyenteRepository contribuyenteRepository;
    private final Importador importador = new Importador();
    private LocalDateTime ultimaConsulta;

    public AgregadorService(HechosRepository hechosRepository, FuentesRepository fuentesRepository, SolicitudesRepository solicitudesRepository, ColeccionRepository coleccionRepository, ContribuyenteRepository contribuyenteRepository) {
        this.hechosRepository = hechosRepository;
        this.fuentesRepository = fuentesRepository;
        this.solicitudesRepository = solicitudesRepository;
        this.coleccionRepository = coleccionRepository;
        this.contribuyenteRepository = contribuyenteRepository;
    }

    /**
     * Gestiona la consulta de hechos por primera vez al iniciar el sistema y,
     * los clasifica inmediatamente.
     */
    @Transactional
    @PostConstruct
    public void consultarHechosPorPrimeraVez() {
        //System.out.print("Se ejecuta el PostConstruct");
        Contribuyente anonimoExistente = contribuyenteRepository.findById(1).orElse(null);

        if (anonimoExistente == null) {
            // Crear e insertar el anónimo con ID manual
            Anonimo anonimo = Anonimo.getInstance();
            contribuyenteRepository.saveAndFlush(anonimo);
        }

        Fuente fuenteExistente = fuentesRepository.findFuenteByNombre("estatica");
        if(fuenteExistente == null){
            FuenteEstatica fuenteEstatica = new FuenteEstatica( "ESTATICA", "http://localhost:8081/api/estatica/hechos", new ArrayList<>());
            Fuente dinamica = new Fuente("http://localhost:8082/api/dinamica/hechos", "DINAMICA");
            //Fuente proxy = new Fuente("http://localhost:8083/api/proxy/hechos", "PROXY");
            fuentesRepository.saveAndFlush(fuenteEstatica);
            fuentesRepository.saveAndFlush(dinamica);
            //fuentesRepository.saveAndFlush(proxy);
        }


        this.consultarHechosPeriodicamente();
        this.clasificarHechos();
    }

    /**
     * Cada una hora, se ejecuta este metodo para consultar los hechos de las fuentes.
     */
    @Transactional
    @Scheduled(fixedRate = 60 * 1000, initialDelay = 30000)
    public void consultarHechosPeriodicamente() {
        System.out.println("Consultando hechos de las fuentes...");

        List<Fuente> fuentes = fuentesRepository.findAll();

        Contribuyente anonimoGestionado = contribuyenteRepository.findById(1).orElse(null);

        fuentes.forEach(fuente -> System.out.println(fuente.hechos));

        fuentes.forEach(fuente -> importador.importarHechos(fuente, this.ultimaConsulta, contribuyenteRepository));
        System.out.print("Ultima consulta: ");
        System.out.println(ultimaConsulta);
        this.ultimaConsulta = LocalDateTime.now();
        fuentes.forEach(fuente -> {

            for (Hecho hecho : fuente.getHechos()) {
                Contribuyente contribuyente = hecho.getContribuyente();
                if (contribuyente != null && contribuyente.getId() != null) {
                    if (contribuyente.getId() == 1) {
                        hecho.setContribuyente(anonimoGestionado);
                    } else {
                        boolean exists = contribuyenteRepository.existsById(contribuyente.getId());
                        if (!exists) {
                            contribuyenteRepository.save(contribuyente);
                        }
                    }
                }
            }

            fuentesRepository.save(fuente);

            //hechosRepository.saveAll(fuente.getHechos());
        }); // TODO: Despues chequear si funciona bien y no guarda repetidos

        //hechosRepository.findAll(); // Se conecta a las otras API's y pone los hechos en instancias de las fuentes
    }

    /**
     * Todos los dias a las 3 AM, se ejecuta este metodo para clasificar los hechos
     * ya importados de las fuentes.
     */

    @Scheduled(cron = "0 0 3 * * *")
    public void clasificarHechos() {
        System.out.println("Clasificando hechos...");
        List<Fuente> fuentes = fuentesRepository.findAll();

        for (Fuente fuente : fuentes) {
            List<Fuente> fuentesParaEvaluar = fuentes.stream()
                    .filter(f -> !f.equals(fuente))  // excluye la fuente actual de la iteración
                    .collect(Collectors.toList());

            List<Hecho> hechos = fuente.getHechos();
            Clasificador.clasificarHechosPorMenciones(hechos, fuentesParaEvaluar, hechosRepository);

            hechosRepository.saveAll(hechos);
            fuente.setHechos(hechos);
        }
    }

    // <----------------- COLECCIONES ----------------->
    // TODO: Implementar manejo de errores y validaciones

    /**
     * Crea una nueva colección a partir del DTO recibido.
     * Si se crea correctamente, devuelve el ID de la colección creada.
     */
    public Integer crearColeccion(ColeccionDTO coleccionDTO){

        List<Fuente> fuentes = fuentesRepository.findAll();

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

    /**
     * Convierte un CriterioDTO a un CriterioPertenencia.
     * Utiliza un switch para determinar el tipo de criterio y crear la instancia correspondiente.
     */
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

    /**
     * Busca todas las colecciones en el repositorio.
     */
    public List<Coleccion> obtenerColecciones() {
        return coleccionRepository.findAll();
    }

    public List<Hecho> obtenerHechosCurados(Integer id, Filtro filtros) {

        Coleccion coleccion = coleccionRepository.findColeccionById(id);

        if (coleccion == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con ID: " + id);
        }
        List<Hecho> hechos =  coleccion.mostrarHechos().stream()
                .filter(hecho -> hecho.consensuado(this.mencionesNecesarias(coleccion.getAlgoritmo_consenso())))
                .collect(Collectors.toList());

        return this.hechosFiltrados(hechos, filtros);
    }

    public List<Hecho> encontrarHechosPorColeccion(
            Integer id,
            Filtro filtros
    ) {

        Coleccion coleccion = coleccionRepository.findColeccionById(id);

        if (coleccion == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con ID: " + id);
        }

        List<Hecho> hechos = coleccion.mostrarHechos();

        return this.hechosFiltrados(hechos, filtros);
    }

    /**
     * Calcula la cantidad de menciones necesarias para que un hecho sea considerado consensuado
     * según el algoritmo de consenso especificado.
     */
    private Double mencionesNecesarias(Algoritmo_Consenso algoritmo) {
        return switch (algoritmo) {
            case MULTIPLES_MENCIONES -> 2.0;
            case MAYORIA_SIMPLE -> Math.floor(this.fuentesRepository.count() / 2.0);
            case ABSOLUTA -> Math.ceil(this.fuentesRepository.count());
            default -> 0.0; // Algoritmo de consenso = 'NINGUNO'
        };
    }

    /**
     * Busca una colección por su ID.
     */
    public Coleccion obtenerColeccion(Integer id){ return this.coleccionRepository.findColeccionById(id);}

    /**
     * Elimina una colección por su ID.
     */
    public void eliminarColeccionPorId(Integer id) { this.coleccionRepository.deleteById(id);}

    /**
     * Actualiza una colección existente con los datos del DTO proporcionado.
     */
    public Coleccion actualizarColeccion(Integer id, ColeccionDTO coleccionDTO) {

        List<CriterioPertenencia> criterios = coleccionDTO.getCriterios().stream()
                .map(this::criterioFromDTO)
                .toList();

        List<Fuente> fuentes = new ArrayList<Fuente>();

        // TODO probar si trae hechos automaticamente o hay q irlos a buscar
        coleccionDTO.getUrls_fuente().forEach( nombreFuente -> {
                fuentes.add(fuentesRepository.findFuenteByNombre(nombreFuente));
            }
        );

        Coleccion coleccionEditada = new Coleccion(
                coleccionDTO.getTitulo(),
                coleccionDTO.getDescripcion(),
                coleccionDTO.getAlgoritmo_consenso(),
                fuentes,
                criterios
        );

        return this.coleccionRepository.save(coleccionEditada);
    }

    /**
     * Modifica el algoritmo de consenso de una colección existente a partir de su ID.
     */
    public Coleccion modificarAlgoritmoConsenso(Integer id, Algoritmo_Consenso nuevoAlgoritmo){
        Coleccion coleccionAModificadar = this.coleccionRepository.findColeccionById(id);
        if (coleccionAModificadar == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con ID: " + id);
        }
        coleccionAModificadar.setAlgoritmo_consenso(nuevoAlgoritmo);
        return this.coleccionRepository.save(coleccionAModificadar);
    }

    /**
     * Modifica la lista de fuentes de una colección existente a partir de su ID.
     * Recibe una lista de URLs (string) de fuentes y actualiza la colección con las fuentes encontradas.
     */
    public Coleccion modificarListaDeFuentes(Integer id, List<String> urls_fuente) {
        Coleccion coleccionAModificadar = this.coleccionRepository.findColeccionById(id);
        if (coleccionAModificadar == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Colección no encontrada con ID: " + id);
        }

        List<Fuente> fuentes = new ArrayList<Fuente>();

        urls_fuente.forEach( nombreFuente -> {
                    fuentes.add(fuentesRepository.findFuenteByNombre(nombreFuente));
                }
        );
        
        coleccionAModificadar.setFuentes(fuentes);

        return this.coleccionRepository.save(coleccionAModificadar);

    }

    public SolicitudEliminacion modificarEstadoSolicitud(Integer id, Estado_Solicitud nuevoEstado) {

        SolicitudEliminacion solicitudAEditar = solicitudesRepository.findSolicitudEliminacionById(id);

        if (solicitudAEditar == null) {
            throw new IllegalArgumentException("Solicitud no encontrada con ID: " + id);
        }

        solicitudAEditar.setEstado(nuevoEstado);

        return solicitudesRepository.save(solicitudAEditar);
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

    public List<Hecho> obtenerTodosLosHechos() {
        return this.hechosRepository.findAll();
    }

    public String obtenerProvinciaConMasHechos(Integer idColeccion) {
        return this.coleccionRepository.obtenerProvinciaConMasHechos(idColeccion);
    }

}



