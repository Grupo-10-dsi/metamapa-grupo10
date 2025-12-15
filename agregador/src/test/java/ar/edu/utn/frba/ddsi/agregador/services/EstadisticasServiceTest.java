package ar.edu.utn.frba.ddsi.agregador.services;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.SolicitudDTOE;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.HechoTextual;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.EstadisticasRepository;
import ar.edu.utn.frba.ddsi.agregador.models.repositories.SolicitudesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstadisticasServiceTest {

    @Mock
    private EstadisticasRepository estadisticasRepository;

    @Mock
    private SolicitudesRepository solicitudesRepository;

    @InjectMocks
    private EstadisticasService estadisticasService;

    private Ubicacion ubicacionTest1;
    private Ubicacion ubicacionTest2;
    private Categoria categoriaTest1;
    private Categoria categoriaTest2;
    private SolicitudEliminacion solicitudSpamTest;

    @BeforeEach
    void setUp() {
        ubicacionTest1 = new Ubicacion(-34.603722, -58.381592);
        ubicacionTest2 = new Ubicacion(-34.608772, -58.373398);

        categoriaTest1 = new Categoria();
        categoriaTest1.setId(1);
        categoriaTest1.setDetalle("Desastre Natural");

        categoriaTest2 = new Categoria();
        categoriaTest2.setId(2);
        categoriaTest2.setDetalle("Accidente");

        // Create a properly initialized hecho for the solicitud
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setDetalle("Test");

        HechoTextual hecho = new HechoTextual();
        hecho.setId(1);
        hecho.setTitulo("Test Hecho");
        hecho.setDescripcion("Descripcion Test");
        hecho.setCategoria(categoria);
        hecho.setUbicacion(ubicacionTest1);

        solicitudSpamTest = new SolicitudEliminacion();
        solicitudSpamTest.setId(1);
        solicitudSpamTest.setJustificacion("Spam detectado - contenido inapropiado que no cumple con las normas");
        solicitudSpamTest.setEstado(Estado_Solicitud.SPAM);
        solicitudSpamTest.setHecho(hecho);
    }

    @Test
    void obtenerUbicacionesColeccion_DeberiaRetornarListaDeUbicaciones() {
        // Arrange
        Integer coleccionId = 1;
        List<Ubicacion> ubicacionesEsperadas = Arrays.asList(ubicacionTest1, ubicacionTest2);
        when(estadisticasRepository.obtenerUbicacionesColeccion(coleccionId))
                .thenReturn(ubicacionesEsperadas);

        // Act
        List<Ubicacion> resultado = estadisticasService.obtenerUbicacionesColeccion(coleccionId);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(-34.603722, resultado.get(0).getLatitud());
        assertEquals(-58.381592, resultado.get(0).getLongitud());
        verify(estadisticasRepository, times(1)).obtenerUbicacionesColeccion(coleccionId);
    }

    @Test
    void obtenerUbicacionesColeccion_ColeccionSinUbicaciones_DeberiaRetornarListaVacia() {
        // Arrange
        Integer coleccionId = 999;
        when(estadisticasRepository.obtenerUbicacionesColeccion(coleccionId))
                .thenReturn(Arrays.asList());

        // Act
        List<Ubicacion> resultado = estadisticasService.obtenerUbicacionesColeccion(coleccionId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(estadisticasRepository, times(1)).obtenerUbicacionesColeccion(coleccionId);
    }

    @Test
    void obtenerCategoriasConMasHechos_DeberiaRetornarTopCategorias() {
        // Arrange
        Integer cantidadCategorias = 5;
        List<Categoria> categoriasEsperadas = Arrays.asList(categoriaTest1, categoriaTest2);
        when(estadisticasRepository.obtenerCategoriasConMasHechos(cantidadCategorias))
                .thenReturn(categoriasEsperadas);

        // Act
        List<Categoria> resultado = estadisticasService.obtenerCategoriasConMasHechos(cantidadCategorias);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Desastre Natural", resultado.get(0).getDetalle());
        assertEquals("Accidente", resultado.get(1).getDetalle());
        verify(estadisticasRepository, times(1)).obtenerCategoriasConMasHechos(cantidadCategorias);
    }

    @Test
    void obtenerCategoriasConMasHechos_SinDatos_DeberiaRetornarListaVacia() {
        // Arrange
        Integer cantidadCategorias = 5;
        when(estadisticasRepository.obtenerCategoriasConMasHechos(cantidadCategorias))
                .thenReturn(Arrays.asList());

        // Act
        List<Categoria> resultado = estadisticasService.obtenerCategoriasConMasHechos(cantidadCategorias);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(estadisticasRepository, times(1)).obtenerCategoriasConMasHechos(cantidadCategorias);
    }

    @Test
    void obtenerUbicacionesCategoria_DeberiaRetornarUbicacionesPorCategoria() {
        // Arrange
        Integer categoriaId = 1;
        List<Ubicacion> ubicacionesEsperadas = Arrays.asList(ubicacionTest1);
        when(estadisticasRepository.obtenerUbicacionesCategoria(categoriaId))
                .thenReturn(ubicacionesEsperadas);

        // Act
        List<Ubicacion> resultado = estadisticasService.obtenerUbicacionesCategoria(categoriaId);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(-34.603722, resultado.get(0).getLatitud());
        verify(estadisticasRepository, times(1)).obtenerUbicacionesCategoria(categoriaId);
    }

    @Test
    void obtenerUbicacionesCategoria_CategoriaSinUbicaciones_DeberiaRetornarListaVacia() {
        // Arrange
        Integer categoriaId = 999;
        when(estadisticasRepository.obtenerUbicacionesCategoria(categoriaId))
                .thenReturn(Arrays.asList());

        // Act
        List<Ubicacion> resultado = estadisticasService.obtenerUbicacionesCategoria(categoriaId);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(estadisticasRepository, times(1)).obtenerUbicacionesCategoria(categoriaId);
    }

    @Test
    void obtenerHorasMasFrecuente_DeberiaRetornarHorasConvertidas() {
        // Arrange
        Integer categoriaId = 1;
        Integer cantidadHoras = 3;
        List<Integer> horasEnteras = Arrays.asList(14, 15, 16);
        when(estadisticasRepository.obtenerHorasMasFrecuente(categoriaId, cantidadHoras))
                .thenReturn(horasEnteras);

        // Act
        List<LocalTime> resultado = estadisticasService.obtenerHorasMasFrecuente(categoriaId, cantidadHoras);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals(LocalTime.of(14, 0), resultado.get(0));
        assertEquals(LocalTime.of(15, 0), resultado.get(1));
        assertEquals(LocalTime.of(16, 0), resultado.get(2));
        verify(estadisticasRepository, times(1)).obtenerHorasMasFrecuente(categoriaId, cantidadHoras);
    }

    @Test
    void obtenerHorasMasFrecuente_SinDatos_DeberiaRetornarListaVacia() {
        // Arrange
        Integer categoriaId = 999;
        Integer cantidadHoras = 3;
        when(estadisticasRepository.obtenerHorasMasFrecuente(categoriaId, cantidadHoras))
                .thenReturn(Arrays.asList());

        // Act
        List<LocalTime> resultado = estadisticasService.obtenerHorasMasFrecuente(categoriaId, cantidadHoras);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(estadisticasRepository, times(1)).obtenerHorasMasFrecuente(categoriaId, cantidadHoras);
    }

    @Test
    void obtenerSolicitudesSpam_DeberiaRetornarSolicitudesConEstadoSpam() {
        // Arrange
        List<SolicitudEliminacion> solicitudesSpam = Arrays.asList(solicitudSpamTest);
        when(solicitudesRepository.findAllByEstado(Estado_Solicitud.SPAM))
                .thenReturn(solicitudesSpam);

        // Act
        List<SolicitudDTOE> resultado = estadisticasService.obtenerSolicitudesSpam();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(solicitudesRepository, times(1)).findAllByEstado(Estado_Solicitud.SPAM);
    }

    @Test
    void obtenerSolicitudesSpam_SinSolicitudesSpam_DeberiaRetornarListaVacia() {
        // Arrange
        when(solicitudesRepository.findAllByEstado(Estado_Solicitud.SPAM))
                .thenReturn(Arrays.asList());

        // Act
        List<SolicitudDTOE> resultado = estadisticasService.obtenerSolicitudesSpam();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(solicitudesRepository, times(1)).findAllByEstado(Estado_Solicitud.SPAM);
    }

    @Test
    void obtenerHorasMasFrecuente_ConHorasLimite_DeberiaConvertirCorrectamente() {
        // Arrange
        Integer categoriaId = 1;
        Integer cantidadHoras = 2;
        List<Integer> horasEnteras = Arrays.asList(0, 23); // Hora inicio y fin del día
        when(estadisticasRepository.obtenerHorasMasFrecuente(categoriaId, cantidadHoras))
                .thenReturn(horasEnteras);

        // Act
        List<LocalTime> resultado = estadisticasService.obtenerHorasMasFrecuente(categoriaId, cantidadHoras);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(LocalTime.of(0, 0), resultado.get(0));
        assertEquals(LocalTime.of(23, 0), resultado.get(1));
        verify(estadisticasRepository, times(1)).obtenerHorasMasFrecuente(categoriaId, cantidadHoras);
    }

    @Test
    void obtenerUbicacionesColeccion_ConMultiplesUbicaciones_DeberiaRetornarTodasLasUbicaciones() {
        // Arrange
        Integer coleccionId = 1;
        Ubicacion ubicacion3 = new Ubicacion(-31.4135, -64.1811);
        List<Ubicacion> ubicacionesEsperadas = Arrays.asList(ubicacionTest1, ubicacionTest2, ubicacion3);
        when(estadisticasRepository.obtenerUbicacionesColeccion(coleccionId))
                .thenReturn(ubicacionesEsperadas);

        // Act
        List<Ubicacion> resultado = estadisticasService.obtenerUbicacionesColeccion(coleccionId);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        verify(estadisticasRepository, times(1)).obtenerUbicacionesColeccion(coleccionId);
    }

    @Test
    void obtenerCategoriasConMasHechos_ConCantidadCero_DeberiaLlamarRepositorio() {
        // Arrange
        Integer cantidadCategorias = 0;
        when(estadisticasRepository.obtenerCategoriasConMasHechos(cantidadCategorias))
                .thenReturn(Arrays.asList());

        // Act
        List<Categoria> resultado = estadisticasService.obtenerCategoriasConMasHechos(cantidadCategorias);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(estadisticasRepository, times(1)).obtenerCategoriasConMasHechos(cantidadCategorias);
    }
}

