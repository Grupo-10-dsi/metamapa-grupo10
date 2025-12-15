package ar.edu.utn.frba.ddsi.proxy.controllers;

import ar.edu.utn.frba.ddsi.proxy.metaMapa.FiltroRequest;
import ar.edu.utn.frba.ddsi.proxy.models.entities.Hecho.Hecho;
import ar.edu.utn.frba.ddsi.proxy.models.entities.solicitudes.SolicitudEliminacion;
import ar.edu.utn.frba.ddsi.proxy.service.HechosServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProxyControllerTest {

    @Mock
    private HechosServices hechosServices;

    @InjectMocks
    private ProxyController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void ObtenerHechos_SinUltimaConsulta_DeberiaRetornarLista() throws Exception {
        // Arrange
        when(hechosServices.findAllHechos(null)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/proxy/hechos"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(hechosServices, times(1)).findAllHechos(null);
    }

    @Test
    void ObtenerHechos_ConUltimaConsulta_DeberiaDelegarService() throws Exception {
        // Arrange
        List<Hecho> hechos = Collections.emptyList();
        when(hechosServices.findAllHechos("2025-12-15T00:00:00")).thenReturn(hechos);

        // Act & Assert
        mockMvc.perform(get("/api/proxy/hechos").param("ultimaConsulta", "2025-12-15T00:00:00"))
                .andExpect(status().isOk());

        verify(hechosServices, times(1)).findAllHechos("2025-12-15T00:00:00");
    }

    @Test
    void ObtenerHechosDemo_DeberiaRetornarLista() throws Exception {
        // Arrange
        when(hechosServices.obtenerHechos("demo")).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/proxy/demo/hechos/{nombreConexion}", "demo"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(hechosServices, times(1)).obtenerHechos("demo");
    }

    @Test
    void ObtenerHechosMetaMapa_DeberiaConstruirFiltroYDelegar() throws Exception {
        // Arrange
        when(hechosServices.obtenerHechosMetaMapa(any(FiltroRequest.class))).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/proxy/metaMapa/hechos")
                        .param("categoria", "cat")
                        .param("fecha_reporte_desde", "2025-12-01T00:00:00")
                        .param("fecha_reporte_hasta", "2025-12-31T23:59:59")
                        .param("fecha_acontecimiento_desde", "2025-12-01T00:00:00")
                        .param("fecha_acontecimiento_hasta", "2025-12-31T23:59:59")
                        .param("latitud", "-34.60")
                        .param("longitud", "-58.38")
                        .param("ultimaConsulta", "2025-12-15T00:00:00"))
                .andExpect(status().isOk());

        verify(hechosServices, times(1)).obtenerHechosMetaMapa(any(FiltroRequest.class));
    }

    @Test
    void ObtenerHechosPorColeccion_DeberiaConstruirFiltroYDelegar() throws Exception {
        // Arrange
        when(hechosServices.obtenerHechosPorColeccion(any(FiltroRequest.class), eq("col1")))
                .thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/proxy/metaMapa/colecciones/{identificador}/hechos", "col1")
                        .param("categoria", "cat"))
                .andExpect(status().isOk());

        verify(hechosServices, times(1)).obtenerHechosPorColeccion(any(FiltroRequest.class), eq("col1"));
    }

    @Test
    void crearSolicitudEliminacion_DeberiaDelegarYRetornarSolicitud() throws Exception {
        // Arrange
        UUID idHecho = UUID.randomUUID();
        SolicitudEliminacion solicitud = new SolicitudEliminacion(idHecho, "justificacion");
        when(hechosServices.crearSolicitudDeEliminacion(eq(idHecho), eq("justificacion")))
                .thenReturn(solicitud);

        String body = "{\"idHecho\": \""+idHecho+"\", \"justificacion\": \"justificacion\"}";

        // Act & Assert
        mockMvc.perform(post("/api/proxy/metaMapa/solicitudes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idHecho").value(idHecho.toString()))
                .andExpect(jsonPath("$.justificacion").value("justificacion"));

        verify(hechosServices, times(1)).crearSolicitudDeEliminacion(eq(idHecho), eq("justificacion"));
    }
}
