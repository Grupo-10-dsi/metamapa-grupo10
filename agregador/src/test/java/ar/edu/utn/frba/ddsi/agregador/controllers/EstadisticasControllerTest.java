package ar.edu.utn.frba.ddsi.agregador.controllers;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.SolicitudDTOE;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Ubicacion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.solicitudEliminacion.Estado_Solicitud;
import ar.edu.utn.frba.ddsi.agregador.services.EstadisticasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EstadisticasControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EstadisticasService estadisticasService;

    @InjectMocks
    private EstadisticasController estadisticasController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(estadisticasController).build();
    }

    @Test
    void obtenerUbicacionesColeccion_DeberiaRetornarListaDeUbicaciones() throws Exception {
        // Arrange
        Integer coleccionId = 1;
        Ubicacion ubicacion1 = new Ubicacion(-34.603722, -58.381592);
        Ubicacion ubicacion2 = new Ubicacion(-34.608772, -58.373398);
        List<Ubicacion> ubicaciones = Arrays.asList(ubicacion1, ubicacion2);

        when(estadisticasService.obtenerUbicacionesColeccion(coleccionId)).thenReturn(ubicaciones);

        // Act & Assert
        mockMvc.perform(get("/agregador/estadisticas/coleccion/{Id}/ubicaciones", coleccionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].latitud").value(-34.603722))
                .andExpect(jsonPath("$[0].longitud").value(-58.381592));

        verify(estadisticasService, times(1)).obtenerUbicacionesColeccion(coleccionId);
    }

    @Test
    void obtenerCategoriasConMasHechos_DeberiaRetornarTopCategorias() throws Exception {
        // Arrange
        Integer cantidadCategorias = 5;
        Categoria cat1 = new Categoria();
        cat1.setId(1);
        cat1.setDetalle("Desastre Natural");

        Categoria cat2 = new Categoria();
        cat2.setId(2);
        cat2.setDetalle("Accidente");

        List<Categoria> categorias = Arrays.asList(cat1, cat2);

        when(estadisticasService.obtenerCategoriasConMasHechos(cantidadCategorias)).thenReturn(categorias);

        // Act & Assert
        mockMvc.perform(get("/agregador/estadisticas/hechos/max-categoria/{cantidadCategorias}", cantidadCategorias))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].detalle").value("Desastre Natural"));

        verify(estadisticasService, times(1)).obtenerCategoriasConMasHechos(cantidadCategorias);
    }

    @Test
    void obtenerUbicacionesCategoria_DeberiaRetornarUbicacionesPorCategoria() throws Exception {
        // Arrange
        Integer categoriaId = 1;
        Ubicacion ubicacion1 = new Ubicacion(-34.603722, -58.381592);
        List<Ubicacion> ubicaciones = Arrays.asList(ubicacion1);

        when(estadisticasService.obtenerUbicacionesCategoria(categoriaId)).thenReturn(ubicaciones);

        // Act & Assert
        mockMvc.perform(get("/agregador/estadisticas/categoria/{Id}/ubicaciones", categoriaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(estadisticasService, times(1)).obtenerUbicacionesCategoria(categoriaId);
    }

    @Test
    void obtenerHorasMasFrecuente_DeberiaRetornarTopHoras() throws Exception {
        // Arrange
        Integer categoriaId = 1;
        Integer cantidadHoras = 3;
        List<LocalTime> horas = Arrays.asList(
                LocalTime.of(14, 0),
                LocalTime.of(15, 0),
                LocalTime.of(16, 0)
        );

        when(estadisticasService.obtenerHorasMasFrecuente(categoriaId, cantidadHoras)).thenReturn(horas);

        // Act & Assert
        // LocalTime is serialized as [hour, minute] or [hour, minute, second] array by Jackson
        mockMvc.perform(get("/agregador/estadisticas/categoria/{Id}/hora/{cantidadHoras}", categoriaId, cantidadHoras))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));

        verify(estadisticasService, times(1)).obtenerHorasMasFrecuente(categoriaId, cantidadHoras);
    }

    @Test
    void obtenerSolicitudesSpam_DeberiaRetornarListaDeSolicitudesSpam() throws Exception {
        // Arrange
        SolicitudDTOE solicitud1 = new SolicitudDTOE(1, 1, "Spam detectado", Estado_Solicitud.SPAM);
        SolicitudDTOE solicitud2 = new SolicitudDTOE(2, 2, "Contenido spam", Estado_Solicitud.SPAM);

        List<SolicitudDTOE> solicitudes = Arrays.asList(solicitud1, solicitud2);

        when(estadisticasService.obtenerSolicitudesSpam()).thenReturn(solicitudes);

        // Act & Assert
        mockMvc.perform(get("/agregador/estadisticas/solicitudes/spam"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].justificacion").value("Spam detectado"));

        verify(estadisticasService, times(1)).obtenerSolicitudesSpam();
    }

    @Test
    void obtenerUbicacionesColeccion_ColeccionSinUbicaciones_DeberiaRetornarListaVacia() throws Exception {
        // Arrange
        Integer coleccionId = 999;
        when(estadisticasService.obtenerUbicacionesColeccion(coleccionId)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/agregador/estadisticas/coleccion/{Id}/ubicaciones", coleccionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(estadisticasService, times(1)).obtenerUbicacionesColeccion(coleccionId);
    }

    @Test
    void obtenerCategoriasConMasHechos_SinCategorias_DeberiaRetornarListaVacia() throws Exception {
        // Arrange
        Integer cantidadCategorias = 5;
        when(estadisticasService.obtenerCategoriasConMasHechos(cantidadCategorias)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/agregador/estadisticas/hechos/max-categoria/{cantidadCategorias}", cantidadCategorias))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(estadisticasService, times(1)).obtenerCategoriasConMasHechos(cantidadCategorias);
    }
}

