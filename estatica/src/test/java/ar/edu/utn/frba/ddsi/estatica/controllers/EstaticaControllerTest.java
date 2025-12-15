package ar.edu.utn.frba.ddsi.estatica.controllers;

import ar.edu.utn.frba.ddsi.estatica.models.entities.dtos.ArchivoProcesadoDTO;
import ar.edu.utn.frba.ddsi.estatica.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.estatica.services.HechosServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EstaticaControllerTest {

    @Mock
    private HechosServices hechosServices;

    @InjectMocks
    private EstaticaController estaticaController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(estaticaController).build();
    }

    @Test
    void listarHechos_ConArchivosValidos_DeberiaRetornarListaDeHechos() throws Exception {
        // Arrange
        List<String> archivos = Arrays.asList("desastres.csv", "terremotos.csv");

        HechoDTO hecho1 = new HechoDTO(1, "Terremoto", "Terremoto en Argentina", null, null,
                LocalDateTime.now(), LocalDateTime.now(), null, null);
        HechoDTO hecho2 = new HechoDTO(2, "Inundacion", "Inundacion en Buenos Aires", null, null,
                LocalDateTime.now(), LocalDateTime.now(), null, null);

        ArchivoProcesadoDTO archivo = new ArchivoProcesadoDTO("desastres.csv", LocalDateTime.now(),
                Arrays.asList(hecho1, hecho2));

        when(hechosServices.obtenerHechos(archivos)).thenReturn(Arrays.asList(archivo));

        // Act & Assert
        mockMvc.perform(get("/api/estatica/hechos")
                .param("archivosProcesados", "desastres.csv", "terremotos.csv"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("desastres.csv"))
                .andExpect(jsonPath("$[0].hechos", hasSize(2)));

        verify(hechosServices, times(1)).obtenerHechos(archivos);
    }

    @Test
    void listarHechos_ConUnArchivoValido_DeberiaRetornarHechosDelArchivo() throws Exception {
        // Arrange
        List<String> archivos = Collections.singletonList("desastres.csv");

        HechoDTO hecho = new HechoDTO(1, "Desastre", "Descripcion", null, null,
                LocalDateTime.now(), LocalDateTime.now(), null, null);

        ArchivoProcesadoDTO archivo = new ArchivoProcesadoDTO("desastres.csv", LocalDateTime.now(),
                Collections.singletonList(hecho));

        when(hechosServices.obtenerHechos(archivos)).thenReturn(Collections.singletonList(archivo));

        // Act & Assert
        mockMvc.perform(get("/api/estatica/hechos")
                .param("archivosProcesados", "desastres.csv"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].hechos", hasSize(1)));

        verify(hechosServices, times(1)).obtenerHechos(archivos);
    }

    @Test
    void listarHechos_SinArchivos_DeberiaRetornarListaVacia() throws Exception {
        // Arrange
        List<String> archivos = Collections.emptyList();

        when(hechosServices.obtenerHechos(archivos)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/estatica/hechos")
                .param("archivosProcesados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(hechosServices, times(1)).obtenerHechos(archivos);
    }

    @Test
    void listarHechos_ConMultiplesArchivos_DeberiaRetornarTodosLosHechos() throws Exception {
        // Arrange
        List<String> archivos = Arrays.asList("archivo1.csv", "archivo2.csv", "archivo3.csv");

        HechoDTO hecho1 = new HechoDTO(1, "Hecho1", "Descripcion1", null, null,
                LocalDateTime.now(), LocalDateTime.now(), null, null);
        HechoDTO hecho2 = new HechoDTO(2, "Hecho2", "Descripcion2", null, null,
                LocalDateTime.now(), LocalDateTime.now(), null, null);

        ArchivoProcesadoDTO archivo1 = new ArchivoProcesadoDTO("archivo1.csv", LocalDateTime.now(),
                Collections.singletonList(hecho1));
        ArchivoProcesadoDTO archivo2 = new ArchivoProcesadoDTO("archivo2.csv", LocalDateTime.now(),
                Collections.singletonList(hecho2));

        when(hechosServices.obtenerHechos(archivos)).thenReturn(Arrays.asList(archivo1, archivo2));

        // Act & Assert
        mockMvc.perform(get("/api/estatica/hechos")
                .param("archivosProcesados", "archivo1.csv", "archivo2.csv", "archivo3.csv"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(hechosServices, times(1)).obtenerHechos(archivos);
    }

    @Test
    void listarHechos_ArchivoProcesadoSinHechos_DeberiaRetornarArchivoConListaVacia() throws Exception {
        // Arrange
        List<String> archivos = Collections.singletonList("archivo_vacio.csv");

        ArchivoProcesadoDTO archivo = new ArchivoProcesadoDTO("archivo_vacio.csv", LocalDateTime.now(),
                Collections.emptyList());

        when(hechosServices.obtenerHechos(archivos)).thenReturn(Collections.singletonList(archivo));

        // Act & Assert
        mockMvc.perform(get("/api/estatica/hechos")
                .param("archivosProcesados", "archivo_vacio.csv"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].hechos", hasSize(0)));

        verify(hechosServices, times(1)).obtenerHechos(archivos);
    }
}

