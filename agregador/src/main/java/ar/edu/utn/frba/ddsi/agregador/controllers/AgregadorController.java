package ar.edu.utn.frba.ddsi.agregador.controllers;


import ar.edu.utn.frba.ddsi.agregador.mappers.CategoriaMapper;
import ar.edu.utn.frba.ddsi.agregador.mappers.ColeccionMapper;
import ar.edu.utn.frba.ddsi.agregador.mappers.HechoMapper;
import ar.edu.utn.frba.ddsi.agregador.mappers.SolicitudMapper;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.*;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Filtro;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.services.AgregadorService;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;


@RequestMapping("/agregador")
@RestController
public class AgregadorController {
    private final AgregadorService agregadorService;
    private final ColeccionMapper coleccionMapper;
    private final CategoriaMapper categoriaMapper;
    private final HechoMapper hechoMapper;
    private final SolicitudMapper solicitudMapper;

    public AgregadorController(AgregadorService agregadorService, ColeccionMapper coleccionMapper, CategoriaMapper categoriaMapper, HechoMapper hechoMapper, SolicitudMapper solicitudMapper) {
        this.agregadorService = agregadorService;
        this.coleccionMapper = coleccionMapper;
        this.categoriaMapper = categoriaMapper;
        this.hechoMapper = hechoMapper;
        this.solicitudMapper = solicitudMapper;
    }

    // <----------------- OPERACIONES CRUD SOBRE COLECCIONES ----------------->
    /**
     * Crea una nueva coleccion en el sistema a partir del DTO que recibe.
     * Si se crea correctamente, devuelve el ID de la coleccion creada.
     */
    @PostMapping("/colecciones")
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public Integer crearColeccion(@RequestBody ColeccionDTO coleccion) {
        return this.agregadorService.crearColeccion(coleccion);
    }

    @GetMapping("/hechos/{id}")
    public HechoDTO obtenerHechoPorId(@PathVariable Integer id) {
        return this.hechoMapper.toHechoDTO(this.agregadorService.obtenerHechoPorId(id));
    }

    /**
     * Devuelve una lista de colecciones dependiendo de los parametros enviados en la req.
     * Si no se envian parametros, devuelve todas las colecciones.
     */

    @GetMapping("/colecciones")
    public List<ColeccionDTO> obtenerColecciones(){
        return this.agregadorService.obtenerColecciones()
                .stream()
                .map(coleccionMapper::toColeccionDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/categorias")
    public List<CategoriaDTO> obtenerCategorias(){
        return this.agregadorService.obtenerCategorias()
                .stream()
                .map(categoriaMapper::toCategoriaDTO)
                .collect(Collectors.toList());
    }
    /**
     * Devuelve una coleccion en particular a partir de su ID.
     * Si no se encuentra la coleccion, devuelve un error 404.
     */
    @GetMapping("/colecciones/{id}")
    public ColeccionDTO obtenerColeccion(@PathVariable Integer id){
        return this.coleccionMapper.toColeccionDTO(this.agregadorService.obtenerColeccion(id));
    }

    /**
     * Elimina una coleccion del sistema a partir de su ID. No devuelve contenido.
     * Si no se encuentra la coleccion, devuelve un error 404.
     */

    @DeleteMapping("/colecciones/{id}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void eliminarColeccion(@PathVariable Integer id) {
        this.agregadorService.eliminarColeccionPorId(id);
    }

    /**
     * Permite modificar una coleccion.
     * Recibe el ID de la coleccion a modificar y un DTO con los nuevos datos.
     */
//    @PutMapping("/colecciones/{id}")
//    @ResponseStatus(org.springframework.http.HttpStatus.OK)
//    public Coleccion modificarColeccion(@PathVariable Integer id, @RequestBody ColeccionDTO coleccionDTO) {
//        return this.agregadorService.actualizarColeccion(id, coleccionDTO);
//    }


     //Endpoint para modificar coleccion

    @PatchMapping("/colecciones/{id}")
    public ColeccionDTO modificarColeccion(@PathVariable Integer id, @RequestBody ActualizacionColeccionDTO actualizacionColeccion) {

        if(actualizacionColeccion.getAlgoritmo_consenso() != null) {
            this.coleccionMapper.toColeccionDTO(this.agregadorService.modificarAlgoritmoConsenso(id, actualizacionColeccion.getAlgoritmo_consenso()));
        } if (actualizacionColeccion.getUrls_fuente() != null) {
            this.coleccionMapper.toColeccionDTO(this.agregadorService.modificarListaDeFuentes(id, actualizacionColeccion.getUrls_fuente()));
        } else {
            throw new IllegalArgumentException("Debe proporcionar al menos un campo para actualizar");
        }
        return this.coleccionMapper.toColeccionDTO(this.agregadorService.obtenerColeccion(id));
    }


//    @PatchMapping("/colecciones/{id}")
//    public Coleccion modificarFuentes(@PathVariable Integer id, @RequestBody List<String> urls_fuente) {
//        return this.agregadorService.modificarListaDeFuentes(id, urls_fuente);
//    }

    // Navegacion de forma irrestricta -> No se aplica curacion

    @GetMapping("/colecciones/{id}/hechos")
    public List<HechoDTO> obtenerHechosPorColeccion(@PathVariable Integer id,
                @RequestParam(required = false) String categoria,
                @RequestParam(required = false) String fecha_reporte_desde,
                @RequestParam(required = false) String fecha_reporte_hasta,
                @RequestParam(required = false) String fecha_acontecimiento_desde,
                @RequestParam(required = false) String fecha_acontecimiento_hasta,
                @RequestParam(required = false) Double latitud,
                @RequestParam(required = false) Double longitud,
                @RequestParam String tipoNavegacion) {

        var filtros = new Filtro(
                categoria,
                fecha_reporte_desde,
                fecha_reporte_hasta,
                fecha_acontecimiento_desde,
                fecha_acontecimiento_hasta,
                latitud,
                longitud
        );

        if(tipoNavegacion == null) {
            throw new IllegalArgumentException("El tipo de navegacion es obligatorio");
        }

        if(tipoNavegacion.equals("irrestricta")) {
            return this.agregadorService.encontrarHechosPorColeccion(id, filtros)
                    .stream()
                    .map(hechoMapper::toHechoDTO)
                    .collect(Collectors.toList());
        } else if (tipoNavegacion.equals("curada")) {
            return this.agregadorService.obtenerHechosCurados(id, filtros)
                    .stream()
                    .map(hechoMapper::toHechoDTO)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Tipo de navegacion no soportado: " + tipoNavegacion);
        }
    }


    @GetMapping("/solicitudes")
    public List<SolicitudDTOE> obtenerSolicitudes() {
        return agregadorService.encontrarSolicitudes()
                .stream()
                .map(solicitudMapper::toSolicitudDTOE)
                .collect(Collectors.toList());
    }

    //  ENDPOINT PARA BUSCAR SOLICITUDES PENDIENTES

    @GetMapping("/solicitudes/pendientes")
    public List<SolicitudDTOE> obtenerSolicitudesPendientes() {
        return agregadorService.encontrarSolicitudesPendientes()
                .stream()
                .map(solicitudMapper::toSolicitudDTOE)
                .collect(Collectors.toList());
    }


    // Endpoint para generar solicitudes de eliminacion de hechos le pega metamapa
    @PostMapping("/solicitudes")
    public Integer generarSolicitudEliminacion(@RequestBody SolicitudDTOE solicitudEliminacion) {
        return agregadorService.crearSolicitudEliminacion(solicitudEliminacion);
    }

    // Endpoint para aceptar/rechazar solicitudes de eliminacion

    @PutMapping("/solicitudes/{id}")
    public SolicitudDTOE modificarSolicitud(@PathVariable Integer id, @RequestBody Estado_Solicitud nuevoEstado) {
        return this.solicitudMapper.toSolicitudDTOE(agregadorService.modificarEstadoSolicitud(id, nuevoEstado));

    }


    @GetMapping("/hechos")
    public List<HechoDTO> obtenerTodosLosHechos(@RequestParam(required = false) String categoria,
                                             @RequestParam(required = false) String fecha_reporte_desde,
                                             @RequestParam(required = false) String fecha_reporte_hasta,
                                             @RequestParam(required = false) String fecha_acontecimiento_desde,
                                             @RequestParam(required = false) String fecha_acontecimiento_hasta,
                                             @RequestParam(required = false) Double latitud,
                                             @RequestParam(required = false) Double longitud
                                             ) {

        var filtros = new Filtro(
                categoria,
                fecha_reporte_desde,
                fecha_reporte_hasta,
                fecha_acontecimiento_desde,
                fecha_acontecimiento_hasta,
                latitud,
                longitud
        );

        List<Hecho> unosHechos = this.agregadorService.obtenerTodosLosHechos();
        return this.agregadorService.hechosFiltrados(unosHechos, filtros)
                .stream()
                .map(hechoMapper::toHechoDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<HechoSearchDTO> busquedaTextoLibre(@RequestParam(required = false) String texto) {
        return agregadorService.buscarTextoLibre(texto);
    }

    /* Ubicaciones para mapa front */
    @GetMapping("/hechos/ubicaciones")
    public List<UbicacionParaMapaDTO> obtenerUbicaciones() {
        return this.agregadorService.obtenerUbicaciones();
    }

}

