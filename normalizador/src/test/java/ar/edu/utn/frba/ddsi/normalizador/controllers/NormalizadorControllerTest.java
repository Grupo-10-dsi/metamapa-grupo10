package ar.edu.utn.frba.ddsi.normalizador.controllers;

import ar.edu.utn.frba.ddsi.normalizador.models.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.normalizador.services.NormalizadorService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NormalizadorControllerTest {

    @Mock
    private NormalizadorService normalizadorService;

    @InjectMocks
    private NormalizadorController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void normalizar_DeberiaRetornarHechoNormalizado() throws Exception {
        // Arrange
        HechoDTO entrada = new HechoDTO();
        entrada.setId(1);
        entrada.setTitulo("titulo crudo");
        entrada.setDescripcion("descripcion cruda");

        HechoDTO salida = new HechoDTO();
        salida.setId(1);
        salida.setTitulo("titulo normalizado");
        salida.setDescripcion("descripcion normalizada");

        when(normalizadorService.normalizar(any(HechoDTO.class))).thenReturn(salida);

        // Act & Assert
        mockMvc.perform(patch("/normalizador/normalizar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("titulo normalizado"))
                .andExpect(jsonPath("$.descripcion").value("descripcion normalizada"));

        verify(normalizadorService, times(1)).normalizar(any(HechoDTO.class));
    }

    @Test
    void health_DeberiaRetornarMensajeOk() throws Exception {
        mockMvc.perform(get("/normalizador/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Servicio de normalizacion activo"));
    }
}

