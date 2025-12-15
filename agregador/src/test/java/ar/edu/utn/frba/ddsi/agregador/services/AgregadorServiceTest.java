package ar.edu.utn.frba.ddsi.agregador.services;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Algoritmo_Consenso;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.criterios.CriterioPertenencia;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.ColeccionDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.CriterioDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.HechoSearchDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.SolicitudDTOE;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.UbicacionParaMapaDTO;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.*;
import ar.edu.utn.frba.ddsi.agregador.models.entities.importador.Importador;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgregadorServiceTest {

    @Mock
    private HechosRepository hechosRepository;

    @Mock
    private FuentesRepository fuentesRepository;

    @Mock
    private SolicitudesRepository solicitudesRepository;

    @Mock
    private ColeccionRepository coleccionRepository;

    @Mock
    private ContribuyenteRepository contribuyenteRepository;

    @Mock
    private ArchivoProcesadoRepository archivoProcesadoRepository;

    @Mock
    private OrigenFuenteRepository origenFuenteRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private Importador importador;

    @InjectMocks
    private AgregadorService agregadorService;

    private Coleccion coleccionTest;
    private ColeccionDTO coleccionDTOTest;
    private Fuente fuenteTest;
    private HechoTextual hechoTest;
    private SolicitudEliminacion solicitudTest;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        fuenteTest = new Fuente("http://test.com", "TestFuente");
        fuenteTest.setUrl("http://test.com");

        coleccionTest = new Coleccion();
        coleccionTest.setId(1);
        coleccionTest.setTitulo("Coleccion Test");
        coleccionTest.setDescripcion("Descripcion test");
        coleccionTest.setAlgoritmo_consenso(Algoritmo_Consenso.MULTIPLES_MENCIONES);
        coleccionTest.setFuentes(Arrays.asList(fuenteTest));
        coleccionTest.setCriterios(new ArrayList<>());

        coleccionDTOTest = new ColeccionDTO(
                null,
                "Nueva Coleccion",
                "Descripcion nueva",
                Algoritmo_Consenso.MAYORIA_SIMPLE,
                Arrays.asList("http://test.com"),
                new ArrayList<>()
        );

        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setDetalle("Test");

        hechoTest = new HechoTextual();
        hechoTest.setId(1);
        hechoTest.setTitulo("Hecho Test");
        hechoTest.setDescripcion("Descripcion test");
        hechoTest.setCategoria(categoria);
        hechoTest.setFechaCarga(LocalDateTime.now());
        hechoTest.setCantidadMenciones(2);

        solicitudTest = new SolicitudEliminacion();
        solicitudTest.setId(1);
        solicitudTest.setJustificacion("Motivo test");
        solicitudTest.setEstado(Estado_Solicitud.PENDIENTE);
    }

    @Test
    void crearColeccion_DeberiaCrearYRetornarId() {
        // Arrange
        when(fuentesRepository.findFuenteByUrl(anyString())).thenReturn(fuenteTest);
        when(coleccionRepository.save(any(Coleccion.class))).thenAnswer(invocation -> {
            Coleccion col = invocation.getArgument(0);
            col.setId(1);
            return col;
        });

        // Act
        Integer resultado = agregadorService.crearColeccion(coleccionDTOTest);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado);
        verify(coleccionRepository, times(1)).save(any(Coleccion.class));
    }

    @Test
    void obtenerColecciones_DeberiaRetornarListaDeColecciones() {
        // Arrange
        List<Coleccion> colecciones = Arrays.asList(coleccionTest);
        when(coleccionRepository.findAll()).thenReturn(colecciones);

        // Act
        List<Coleccion> resultado = agregadorService.obtenerColecciones();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Coleccion Test", resultado.get(0).getTitulo());
        verify(coleccionRepository, times(1)).findAll();
    }

    @Test
    void obtenerColeccion_ConIdValido_DeberiaRetornarColeccion() {
        // Arrange
        when(coleccionRepository.findColeccionById(1)).thenReturn(coleccionTest);

        // Act
        Coleccion resultado = agregadorService.obtenerColeccion(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Coleccion Test", resultado.getTitulo());
        verify(coleccionRepository, times(1)).findColeccionById(1);
    }

    @Test
    void obtenerColeccion_ConIdInvalido_DeberiaLanzarExcepcion() {
        // Arrange
        when(coleccionRepository.findColeccionById(999)).thenReturn(null);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            agregadorService.obtenerColeccion(999);
        });
        verify(coleccionRepository, times(1)).findColeccionById(999);
    }

    @Test
    void eliminarColeccionPorId_ConIdValido_DeberiaEliminarColeccion() {
        // Arrange
        when(coleccionRepository.findColeccionById(1)).thenReturn(coleccionTest);
        doNothing().when(coleccionRepository).deleteById(1);

        // Act
        agregadorService.eliminarColeccionPorId(1);

        // Assert
        verify(coleccionRepository, times(1)).findColeccionById(1);
        verify(coleccionRepository, times(1)).deleteById(1);
    }

    @Test
    void eliminarColeccionPorId_ConIdInvalido_DeberiaLanzarExcepcion() {
        // Arrange
        when(coleccionRepository.findColeccionById(999)).thenReturn(null);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            agregadorService.eliminarColeccionPorId(999);
        });
        verify(coleccionRepository, times(1)).findColeccionById(999);
        verify(coleccionRepository, never()).deleteById(anyInt());
    }

    @Test
    void obtenerCategorias_DeberiaRetornarListaDeCategorias() {
        // Arrange
        Categoria cat1 = new Categoria();
        cat1.setId(1);
        cat1.setDetalle("Categoria 1");

        Categoria cat2 = new Categoria();
        cat2.setId(2);
        cat2.setDetalle("Categoria 2");

        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(cat1, cat2));

        // Act
        List<Categoria> resultado = agregadorService.obtenerCategorias();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void obtenerHechoPorId_ConIdValido_DeberiaRetornarHecho() {
        // Arrange
        when(hechosRepository.findHechoById(1)).thenReturn(hechoTest);

        // Act
        Hecho resultado = agregadorService.obtenerHechoPorId(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Hecho Test", resultado.getTitulo());
        verify(hechosRepository, times(1)).findHechoById(1);
    }

    @Test
    void obtenerHechoPorId_ConIdInvalido_DeberiaLanzarExcepcion() {
        // Arrange
        when(hechosRepository.findHechoById(999)).thenReturn(null);

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            agregadorService.obtenerHechoPorId(999);
        });
        verify(hechosRepository, times(1)).findHechoById(999);
    }

    @Test
    void modificarAlgoritmoConsenso_DeberiaActualizarAlgoritmo() {
        // Arrange
        when(coleccionRepository.findColeccionById(1)).thenReturn(coleccionTest);
        when(coleccionRepository.save(any(Coleccion.class))).thenReturn(coleccionTest);

        // Act
        agregadorService.modificarAlgoritmoConsenso(1, Algoritmo_Consenso.ABSOLUTA);

        // Assert
        assertEquals(Algoritmo_Consenso.ABSOLUTA, coleccionTest.getAlgoritmo_consenso());
        verify(coleccionRepository, times(1)).findColeccionById(1);
        verify(coleccionRepository, times(1)).save(coleccionTest);
    }

    @Test
    void modificarListaDeFuentes_DeberiaActualizarFuentes() {
        // Arrange
        List<String> nuevasFuentes = Arrays.asList("http://nueva-fuente.com");
        Fuente nuevaFuente = new Fuente("http://nueva-fuente.com", "NuevaFuente");

        when(coleccionRepository.findColeccionById(1)).thenReturn(coleccionTest);
        when(fuentesRepository.findFuenteByUrl("http://nueva-fuente.com")).thenReturn(nuevaFuente);
        when(coleccionRepository.save(any(Coleccion.class))).thenReturn(coleccionTest);

        // Act
        agregadorService.modificarListaDeFuentes(1, nuevasFuentes);

        // Assert
        verify(coleccionRepository, times(1)).findColeccionById(1);
        verify(fuentesRepository, times(1)).findFuenteByUrl("http://nueva-fuente.com");
        verify(coleccionRepository, times(1)).save(coleccionTest);
    }

    @Test
    void crearSolicitudEliminacion_DeberiaCrearYRetornarId() {
        // Arrange
        String justificacionLarga = "Esta es una justificación muy larga y detallada sobre por qué se desea eliminar este hecho. " +
                "Contiene suficientes caracteres para no ser considerada spam y cumple con los requisitos de la solicitud. " +
                "Se proporciona información detallada sobre los motivos de la eliminación del contenido en cuestión. " +
                "Este texto tiene más de 500 caracteres como se requiere en la validación.";
        SolicitudDTOE solicitudDTO = new SolicitudDTOE(null, 1, justificacionLarga, Estado_Solicitud.PENDIENTE);

        when(hechosRepository.findById(1)).thenReturn(Optional.of(hechoTest));
        when(solicitudesRepository.save(any(SolicitudEliminacion.class))).thenAnswer(invocation -> {
            SolicitudEliminacion sol = invocation.getArgument(0);
            sol.setId(1);
            return sol;
        });

        // Act
        Integer resultado = agregadorService.crearSolicitudEliminacion(solicitudDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado);
        verify(solicitudesRepository, times(1)).save(any(SolicitudEliminacion.class));
    }

    @Test
    void crearSolicitudEliminacion_ConHechoInexistente_DeberiaLanzarExcepcion() {
        // Arrange
        SolicitudDTOE solicitudDTO = new SolicitudDTOE(null, 999, "Informacion incorrecta", Estado_Solicitud.PENDIENTE);

        when(hechosRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            agregadorService.crearSolicitudEliminacion(solicitudDTO);
        });
        verify(solicitudesRepository, never()).save(any(SolicitudEliminacion.class));
    }

    @Test
    void encontrarSolicitudes_DeberiaRetornarListaDeSolicitudes() {
        // Arrange
        when(solicitudesRepository.findAll()).thenReturn(Arrays.asList(solicitudTest));

        // Act
        List<SolicitudEliminacion> resultado = agregadorService.encontrarSolicitudes();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(solicitudesRepository, times(1)).findAll();
    }

    @Test
    void encontrarSolicitudesPendientes_DeberiaRetornarSoloSolicitudesPendientes() {
        // Arrange
        when(solicitudesRepository.findAllByEstado(Estado_Solicitud.PENDIENTE))
                .thenReturn(Arrays.asList(solicitudTest));

        // Act
        List<SolicitudEliminacion> resultado = agregadorService.encontrarSolicitudesPendientes();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(Estado_Solicitud.PENDIENTE, resultado.get(0).getEstado());
        verify(solicitudesRepository, times(1)).findAllByEstado(Estado_Solicitud.PENDIENTE);
    }

    @Test
    void modificarEstadoSolicitud_DeberiaActualizarEstado() {
        // Arrange
        when(solicitudesRepository.findById(1)).thenReturn(Optional.of(solicitudTest));
        when(solicitudesRepository.save(any(SolicitudEliminacion.class))).thenReturn(solicitudTest);

        // Act
        SolicitudEliminacion resultado = agregadorService.modificarEstadoSolicitud(1, Estado_Solicitud.ACEPTADA);

        // Assert
        assertNotNull(resultado);
        assertEquals(Estado_Solicitud.ACEPTADA, resultado.getEstado());
        verify(solicitudesRepository, times(1)).findById(1);
        verify(solicitudesRepository, times(1)).save(solicitudTest);
    }

    @Test
    void modificarEstadoSolicitud_ConIdInvalido_DeberiaLanzarExcepcion() {
        // Arrange
        when(solicitudesRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> {
            agregadorService.modificarEstadoSolicitud(999, Estado_Solicitud.ACEPTADA);
        });
        verify(solicitudesRepository, times(1)).findById(999);
        verify(solicitudesRepository, never()).save(any(SolicitudEliminacion.class));
    }

    @Test
    void criterioFromDTO_ConTipoTitulo_DeberiaCrearCriterioTitulo() {
        // Arrange
        CriterioDTO criterioDTO = new CriterioDTO("titulo", "Test");

        // Act
        CriterioPertenencia resultado = agregadorService.criterioFromDTO(criterioDTO);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    void criterioFromDTO_ConTipoDescripcion_DeberiaCrearCriterioDescripcion() {
        // Arrange
        CriterioDTO criterioDTO = new CriterioDTO("descripcion", "Test descripcion");

        // Act
        CriterioPertenencia resultado = agregadorService.criterioFromDTO(criterioDTO);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    void criterioFromDTO_ConTipoCategoria_DeberiaCrearCriterioCategoria() {
        // Arrange
        CriterioDTO criterioDTO = new CriterioDTO("categoria", "Desastre Natural");

        // Act
        CriterioPertenencia resultado = agregadorService.criterioFromDTO(criterioDTO);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    void criterioFromDTO_ConTipoFechaDesde_DeberiaCrearCriterioFechaDesde() {
        // Arrange
        CriterioDTO criterioDTO = new CriterioDTO("fecha_desde", "2024-01-01");

        // Act
        CriterioPertenencia resultado = agregadorService.criterioFromDTO(criterioDTO);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    void criterioFromDTO_ConTipoFechaHasta_DeberiaCrearCriterioFechaHasta() {
        // Arrange
        CriterioDTO criterioDTO = new CriterioDTO("fecha_hasta", "2024-12-31");

        // Act
        CriterioPertenencia resultado = agregadorService.criterioFromDTO(criterioDTO);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    void criterioFromDTO_ConTipoUbicacion_DeberiaCrearCriterioUbicacion() {
        // Arrange
        CriterioDTO criterioDTO = new CriterioDTO("ubicacion", "-34.603722,-58.381592");

        // Act
        CriterioPertenencia resultado = agregadorService.criterioFromDTO(criterioDTO);

        // Assert
        assertNotNull(resultado);
    }

    @Test
    void criterioFromDTO_ConTipoDesconocido_DeberiaLanzarExcepcion() {
        // Arrange
        CriterioDTO criterioDTO = new CriterioDTO("tipo_invalido", "valor");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            agregadorService.criterioFromDTO(criterioDTO);
        });
    }

    @Test
    void encontrarHechosPorColeccion_ConColeccionValida_DeberiaRetornarHechos() {
        // Arrange
        List<Hecho> hechos = Arrays.asList(hechoTest);
        fuenteTest.setHechos(hechos);

        when(coleccionRepository.findColeccionById(1)).thenReturn(coleccionTest);

        // Act
        Filtro filtros = new Filtro(null, null, null, null, null, null, null);
        List<Hecho> resultado = agregadorService.encontrarHechosPorColeccion(1, filtros);

        // Assert
        assertNotNull(resultado);
        verify(coleccionRepository, times(1)).findColeccionById(1);
    }

    @Test
    void encontrarHechosPorColeccion_ConColeccionInvalida_DeberiaLanzarExcepcion() {
        // Arrange
        when(coleccionRepository.findColeccionById(999)).thenReturn(null);

        // Act & Assert
        Filtro filtros = new Filtro(null, null, null, null, null, null, null);
        assertThrows(ResponseStatusException.class, () -> {
            agregadorService.encontrarHechosPorColeccion(999, filtros);
        });
        verify(coleccionRepository, times(1)).findColeccionById(999);
    }

    @Test
    void obtenerTodosLosHechos_DeberiaRetornarTodosLosHechos() {
        // Arrange
        List<Hecho> hechos = Arrays.asList(hechoTest);
        when(hechosRepository.findAll()).thenReturn(hechos);

        // Act
        List<Hecho> resultado = agregadorService.obtenerTodosLosHechos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(hechosRepository, times(1)).findAll();
    }

    @Test
    void buscarTextoLibre_ConTextoValido_DeberiaRetornarResultados() {
        // Arrange
        String textoBusqueda = "terremoto";
        List<HechoSearchDTO> resultados = new ArrayList<>();
        when(hechosRepository.findByTexto(textoBusqueda)).thenReturn(resultados);

        // Act
        List<HechoSearchDTO> resultado = agregadorService.buscarTextoLibre(textoBusqueda);

        // Assert
        assertNotNull(resultado);
        verify(hechosRepository, times(1)).findByTexto(textoBusqueda);
    }

    @Test
    void obtenerUbicaciones_DeberiaRetornarListaDeUbicaciones() {
        // Arrange
        List<UbicacionParaMapaDTO> ubicaciones = new ArrayList<>();
        when(hechosRepository.obtenerUbicaciones()).thenReturn(ubicaciones);

        // Act
        List<UbicacionParaMapaDTO> resultado = agregadorService.obtenerUbicaciones();

        // Assert
        assertNotNull(resultado);
        verify(hechosRepository, times(1)).obtenerUbicaciones();
    }

    @Test
    void obtenerHechosPorEtiquetas_ConListaVacia_DeberiaRetornarListaVacia() {
        // Arrange
        List<String> etiquetas = new ArrayList<>();

        // Act
        List<Hecho> resultado = agregadorService.obtenerHechosPorEtiquetas(etiquetas, true);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(hechosRepository, never()).findByEtiquetasAll(any(), anyInt());
    }

    @Test
    void obtenerHechosPorEtiquetas_ConEtiquetasMatchAll_DeberiaRetornarHechosConTodasEtiquetas() {
        // Arrange
        List<String> etiquetas = Arrays.asList("peligro", "urgente");
        List<Hecho> hechos = Arrays.asList(hechoTest);
        when(hechosRepository.findByEtiquetasAll(etiquetas, etiquetas.size())).thenReturn(hechos);

        // Act
        List<Hecho> resultado = agregadorService.obtenerHechosPorEtiquetas(etiquetas, true);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(hechosRepository, times(1)).findByEtiquetasAll(etiquetas, 2);
    }

    @Test
    void obtenerHechosPorEtiquetas_ConEtiquetasMatchAny_DeberiaRetornarHechosConAlgunaEtiqueta() {
        // Arrange
        List<String> etiquetas = Arrays.asList("peligro", "urgente");
        List<Hecho> hechos = Arrays.asList(hechoTest);
        when(hechosRepository.findByEtiquetasAny(etiquetas)).thenReturn(hechos);

        // Act
        List<Hecho> resultado = agregadorService.obtenerHechosPorEtiquetas(etiquetas, false);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(hechosRepository, times(1)).findByEtiquetasAny(etiquetas);
    }
}
