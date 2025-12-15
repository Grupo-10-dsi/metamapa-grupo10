package ar.edu.utn.frba.ddsi.agregador.controllers;

import ar.edu.utn.frba.ddsi.agregador.mappers.CategoriaMapper;
import ar.edu.utn.frba.ddsi.agregador.mappers.ColeccionMapper;
import ar.edu.utn.frba.ddsi.agregador.mappers.HechoMapper;
import ar.edu.utn.frba.ddsi.agregador.mappers.SolicitudMapper;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Algoritmo_Consenso;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.*;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Filtro;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.HechoTextual;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.agregador.services.AgregadorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AgregadorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AgregadorService agregadorService;

    @Mock
    private ColeccionMapper coleccionMapper;

    @Mock
    private CategoriaMapper categoriaMapper;

    @Mock
    private HechoMapper hechoMapper;

    @Mock
    private SolicitudMapper solicitudMapper;

    @InjectMocks
    private AgregadorController agregadorController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(agregadorController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void crearColeccion_DeberiaRetornarIdCreado() throws Exception {
        // Arrange
        ColeccionDTO coleccionDTO = new ColeccionDTO(
                null,
                "Test Coleccion",
                "Descripcion de prueba",
                Algoritmo_Consenso.MULTIPLES_MENCIONES,
                Arrays.asList("http://fuente1.com", "http://fuente2.com"),
                new ArrayList<>()
        );

        when(agregadorService.crearColeccion(any(ColeccionDTO.class))).thenReturn(1);

        // Act & Assert
        mockMvc.perform(post("/agregador/colecciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(coleccionDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));

        verify(agregadorService, times(1)).crearColeccion(any(ColeccionDTO.class));
    }

    @Test
    void obtenerColecciones_DeberiaRetornarListaDeColecciones() throws Exception {
        // Arrange
        Coleccion coleccion1 = new Coleccion();
        coleccion1.setId(1);
        coleccion1.setTitulo("Coleccion 1");

        Coleccion coleccion2 = new Coleccion();
        coleccion2.setId(2);
        coleccion2.setTitulo("Coleccion 2");

        List<Coleccion> colecciones = Arrays.asList(coleccion1, coleccion2);

        when(agregadorService.obtenerColecciones()).thenReturn(colecciones);

        // Act & Assert
        mockMvc.perform(get("/agregador/colecciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(agregadorService, times(1)).obtenerColecciones();
    }

    @Test
    void obtenerColeccion_ConIdValido_DeberiaRetornarColeccion() throws Exception {
        // Arrange
        Integer id = 1;
        Coleccion coleccion = new Coleccion();
        coleccion.setId(id);
        coleccion.setTitulo("Test Coleccion");

        when(agregadorService.obtenerColeccion(id)).thenReturn(coleccion);

        // Act & Assert
        mockMvc.perform(get("/agregador/colecciones/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.titulo").value("Test Coleccion"));

        verify(agregadorService, times(1)).obtenerColeccion(id);
    }

    @Test
    void eliminarColeccion_DeberiaRetornarNoContent() throws Exception {
        // Arrange
        Integer id = 1;
        doNothing().when(agregadorService).eliminarColeccionPorId(id);

        // Act & Assert
        mockMvc.perform(delete("/agregador/colecciones/{id}", id))
                .andExpect(status().isNoContent());

        verify(agregadorService, times(1)).eliminarColeccionPorId(id);
    }

    @Test
    void modificarColeccion_ConAlgoritmoConsenso_DeberiaRetornarColeccionModificada() throws Exception {
        // Arrange
        Integer id = 1;
        ActualizacionColeccionDTO actualizacion = new ActualizacionColeccionDTO(
                Algoritmo_Consenso.MAYORIA_SIMPLE,
                Arrays.asList("http://fuente-actualizada.com")
        );

        Coleccion coleccionModificada = new Coleccion();
        coleccionModificada.setId(id);
        coleccionModificada.setAlgoritmo_consenso(Algoritmo_Consenso.MAYORIA_SIMPLE);

        when(agregadorService.modificarAlgoritmoConsenso(eq(id), any(Algoritmo_Consenso.class)))
                .thenReturn(coleccionModificada);
        when(agregadorService.obtenerColeccion(id)).thenReturn(coleccionModificada);

        // Act & Assert
        mockMvc.perform(patch("/agregador/colecciones/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(agregadorService, times(1)).modificarAlgoritmoConsenso(eq(id), any(Algoritmo_Consenso.class));
    }

    @Test
    void obtenerCategorias_DeberiaRetornarListaDeCategorias() throws Exception {
        // Arrange
        Categoria cat1 = new Categoria();
        cat1.setId(1);
        cat1.setDetalle("Categoria 1");

        Categoria cat2 = new Categoria();
        cat2.setId(2);
        cat2.setDetalle("Categoria 2");

        List<Categoria> categorias = Arrays.asList(cat1, cat2);

        CategoriaDTO dto1 = new CategoriaDTO(1, "Categoria 1");
        CategoriaDTO dto2 = new CategoriaDTO(2, "Categoria 2");

        when(agregadorService.obtenerCategorias()).thenReturn(categorias);
        when(categoriaMapper.toCategoriaDTO(cat1)).thenReturn(dto1);
        when(categoriaMapper.toCategoriaDTO(cat2)).thenReturn(dto2);

        // Act & Assert
        mockMvc.perform(get("/agregador/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(agregadorService, times(1)).obtenerCategorias();
    }

    @Test
    void obtenerHechosPorColeccion_ConTipoNavegacionIrrestricta_DeberiaRetornarHechos() throws Exception {
        // Arrange
        Integer coleccionId = 1;
        List<Hecho> hechos = new ArrayList<>();

        when(agregadorService.encontrarHechosPorColeccion(eq(coleccionId), any(Filtro.class)))
                .thenReturn(hechos);

        // Act & Assert
        mockMvc.perform(get("/agregador/colecciones/{id}/hechos", coleccionId)
                        .param("tipoNavegacion", "irrestricta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(agregadorService, times(1)).encontrarHechosPorColeccion(eq(coleccionId), any(Filtro.class));
    }

    @Test
    void obtenerHechosPorColeccion_ConTipoNavegacionCurada_DeberiaRetornarHechosCurados() throws Exception {
        // Arrange
        Integer coleccionId = 1;
        List<Hecho> hechos = new ArrayList<>();

        when(agregadorService.obtenerHechosCurados(eq(coleccionId), any(Filtro.class)))
                .thenReturn(hechos);

        // Act & Assert
        mockMvc.perform(get("/agregador/colecciones/{id}/hechos", coleccionId)
                        .param("tipoNavegacion", "curada"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(agregadorService, times(1)).obtenerHechosCurados(eq(coleccionId), any(Filtro.class));
    }

    @Test
    void generarSolicitudEliminacion_DeberiaRetornarIdCreado() throws Exception {
        // Arrange
        SolicitudDTOE solicitudDTO = new SolicitudDTOE(null, 1, "Informacion incorrecta", Estado_Solicitud.PENDIENTE);

        when(agregadorService.crearSolicitudEliminacion(any(SolicitudDTOE.class))).thenReturn(1);

        // Act & Assert
        mockMvc.perform(post("/agregador/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(solicitudDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(agregadorService, times(1)).crearSolicitudEliminacion(any(SolicitudDTOE.class));
    }

    @Test
    void obtenerSolicitudesPendientes_DeberiaRetornarListaDeSolicitudes() throws Exception {
        // Arrange
        SolicitudEliminacion solicitud = new SolicitudEliminacion();
        solicitud.setId(1);
        solicitud.setEstado(Estado_Solicitud.PENDIENTE);

        SolicitudDTOE solicitudDTO = new SolicitudDTOE(1, 1, "Justificacion test", Estado_Solicitud.PENDIENTE);

        when(agregadorService.encontrarSolicitudesPendientes()).thenReturn(Arrays.asList(solicitud));
        when(solicitudMapper.toSolicitudDTOE(any(SolicitudEliminacion.class))).thenReturn(solicitudDTO);

        // Act & Assert
        mockMvc.perform(get("/agregador/solicitudes/pendientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(agregadorService, times(1)).encontrarSolicitudesPendientes();
    }

    @Test
    void modificarSolicitud_DeberiaRetornarSolicitudModificada() throws Exception {
        // Arrange
        Integer solicitudId = 1;
        Estado_Solicitud nuevoEstado = Estado_Solicitud.ACEPTADA;

        SolicitudEliminacion solicitud = new SolicitudEliminacion();
        solicitud.setId(solicitudId);
        solicitud.setEstado(nuevoEstado);

        SolicitudDTOE solicitudDTO = new SolicitudDTOE(solicitudId, 1, "Justificacion", nuevoEstado);

        when(agregadorService.modificarEstadoSolicitud(solicitudId, nuevoEstado)).thenReturn(solicitud);
        when(solicitudMapper.toSolicitudDTOE(solicitud)).thenReturn(solicitudDTO);

        // Act & Assert
        mockMvc.perform(put("/agregador/solicitudes/{id}", solicitudId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoEstado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(solicitudId));

        verify(agregadorService, times(1)).modificarEstadoSolicitud(solicitudId, nuevoEstado);
    }

    @Test
    void obtenerHechoPorId_ConIdValido_DeberiaRetornarHecho() throws Exception {
        // Arrange
        Integer hechoId = 1;
        HechoTextual hecho = new HechoTextual();
        hecho.setId(hechoId);
        hecho.setTitulo("Test Hecho");

        when(agregadorService.obtenerHechoPorId(hechoId)).thenReturn(hecho);

        // Act & Assert
        mockMvc.perform(get("/agregador/hechos/{id}", hechoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hechoId))
                .andExpect(jsonPath("$.titulo").value("Test Hecho"));

        verify(agregadorService, times(1)).obtenerHechoPorId(hechoId);
    }

    @Test
    void obtenerTodosLosHechos_ConFiltros_DeberiaRetornarHechosFiltrados() throws Exception {
        // Arrange
        List<Hecho> hechos = new ArrayList<>();
        List<HechoDTOGraph> hechosDTO = new ArrayList<>();

        when(agregadorService.obtenerTodosLosHechos()).thenReturn(hechos);
        when(agregadorService.hechosFiltrados(any(), any(Filtro.class))).thenReturn(hechos);

        // Act & Assert
        mockMvc.perform(get("/agregador/hechos")
                        .param("categoria", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(agregadorService, times(1)).obtenerTodosLosHechos();
        verify(agregadorService, times(1)).hechosFiltrados(any(), any(Filtro.class));
    }

    @Test
    void busquedaTextoLibre_DeberiaRetornarHechosBuscados() throws Exception {
        // Arrange
        String textoBusqueda = "terremoto";
        List<HechoSearchDTO> resultados = new ArrayList<>();

        when(agregadorService.buscarTextoLibre(textoBusqueda)).thenReturn(resultados);

        // Act & Assert
        mockMvc.perform(get("/agregador/search")
                        .param("texto", textoBusqueda))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(agregadorService, times(1)).buscarTextoLibre(textoBusqueda);
    }

    @Test
    void obtenerUbicaciones_DeberiaRetornarListaDeUbicaciones() throws Exception {
        // Arrange
        List<UbicacionParaMapaDTO> ubicaciones = new ArrayList<>();

        when(agregadorService.obtenerUbicaciones()).thenReturn(ubicaciones);

        // Act & Assert
        mockMvc.perform(get("/agregador/hechos/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(agregadorService, times(1)).obtenerUbicaciones();
    }
}

