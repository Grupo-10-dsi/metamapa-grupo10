package ar.edu.utn.frba.ddsi.estatica.services;

import ar.edu.utn.frba.ddsi.estatica.models.entities.ArchivoProcesado.ArchivoProcesado;
import ar.edu.utn.frba.ddsi.estatica.models.entities.dtos.ArchivoProcesadoDTO;
import ar.edu.utn.frba.ddsi.estatica.models.entities.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.repositories.HechosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HechosServicesTest {

    @Mock
    private HechosRepository hechosRepository;

    @InjectMocks
    private HechosServices hechosServices;

    private Hecho hechoTest;
    private ArchivoProcesado archivoProcesadoTest;

    @BeforeEach
    void setUp() {
        hechoTest = new Hecho("Terremoto", "Terremoto en Argentina", null, null, LocalDateTime.now());
        hechoTest.setId(1);

        archivoProcesadoTest = new ArchivoProcesado("desastres.csv", LocalDateTime.now(),
                Collections.singletonList(hechoTest));
    }

    @Test
    void obtenerHechos_ConArchivosValidos_DeberiaRetornarArchivosProcesados() {
        // Arrange
        List<String> nombresArchivos = Arrays.asList("desastres.csv", "terremotos.csv");

        when(hechosRepository.findAllArchivosProcesados())
                .thenReturn(Collections.singletonList(archivoProcesadoTest));

        // Act
        List<ArchivoProcesadoDTO> resultado = hechosServices.obtenerHechos(nombresArchivos);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("desastres.csv", resultado.get(0).getNombre());
        assertEquals(1, resultado.get(0).getHechos().size());

        verify(hechosRepository, times(1)).importarHechosSin(nombresArchivos);
        verify(hechosRepository, times(1)).findAllArchivosProcesados();
    }

    @Test
    void obtenerHechos_SinArchivos_DeberiaRetornarListaVacia() {
        // Arrange
        List<String> nombresArchivos = Collections.emptyList();

        when(hechosRepository.findAllArchivosProcesados()).thenReturn(Collections.emptyList());

        // Act
        List<ArchivoProcesadoDTO> resultado = hechosServices.obtenerHechos(nombresArchivos);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(hechosRepository, times(1)).importarHechosSin(nombresArchivos);
        verify(hechosRepository, times(1)).findAllArchivosProcesados();
    }

    @Test
    void obtenerHechos_ConMultiplesArchivos_DeberiaRetornarTodosLosArchivosProcesados() {
        // Arrange
        List<String> nombresArchivos = Arrays.asList("archivo1.csv", "archivo2.csv");

        Hecho hecho2 = new Hecho("Inundacion", "Inundacion en Buenos Aires", null, null, LocalDateTime.now());
        hecho2.setId(2);

        ArchivoProcesado archivo2 = new ArchivoProcesado("terremotos.csv", LocalDateTime.now(),
                Collections.singletonList(hecho2));

        when(hechosRepository.findAllArchivosProcesados())
                .thenReturn(Arrays.asList(archivoProcesadoTest, archivo2));

        // Act
        List<ArchivoProcesadoDTO> resultado = hechosServices.obtenerHechos(nombresArchivos);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("desastres.csv", resultado.get(0).getNombre());
        assertEquals("terremotos.csv", resultado.get(1).getNombre());

        verify(hechosRepository, times(1)).importarHechosSin(nombresArchivos);
    }

    @Test
    void obtenerHechos_ArchivoProcesadoSinHechos_DeberiaRetornarArchivoVacio() {
        // Arrange
        List<String> nombresArchivos = Collections.singletonList("archivo_vacio.csv");

        ArchivoProcesado archivoVacio = new ArchivoProcesado("archivo_vacio.csv", LocalDateTime.now(),
                Collections.emptyList());

        when(hechosRepository.findAllArchivosProcesados())
                .thenReturn(Collections.singletonList(archivoVacio));

        // Act
        List<ArchivoProcesadoDTO> resultado = hechosServices.obtenerHechos(nombresArchivos);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getHechos().isEmpty());

        verify(hechosRepository, times(1)).importarHechosSin(nombresArchivos);
    }

    @Test
    void obtenerHechos_ConHechosMultiples_DeberiaMapearTodosCorrectamente() {
        // Arrange
        List<String> nombresArchivos = Collections.singletonList("desastres.csv");

        Hecho hecho2 = new Hecho("Inundacion", "Inundacion", null, null, LocalDateTime.now());
        hecho2.setId(2);
        Hecho hecho3 = new Hecho("Incendio", "Incendio", null, null, LocalDateTime.now());
        hecho3.setId(3);

        ArchivoProcesado archivoConMultiplesHechos = new ArchivoProcesado("desastres.csv", LocalDateTime.now(),
                Arrays.asList(hechoTest, hecho2, hecho3));

        when(hechosRepository.findAllArchivosProcesados())
                .thenReturn(Collections.singletonList(archivoConMultiplesHechos));

        // Act
        List<ArchivoProcesadoDTO> resultado = hechosServices.obtenerHechos(nombresArchivos);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(3, resultado.get(0).getHechos().size());

        List<HechoDTO> hechos = resultado.get(0).getHechos();
        assertEquals("Terremoto", hechos.get(0).getTitulo());
        assertEquals("Inundacion", hechos.get(1).getTitulo());
        assertEquals("Incendio", hechos.get(2).getTitulo());
    }

    @Test
    void obtenerHechos_ConArchivosProcesadosMultiples_DeberiaRetornarTodos() {
        // Arrange
        List<String> nombresArchivos = Arrays.asList("archivo1.csv", "archivo2.csv", "archivo3.csv");

        Hecho hecho2 = new Hecho("Hecho2", "Desc2", null, null, LocalDateTime.now());
        hecho2.setId(2);
        Hecho hecho3 = new Hecho("Hecho3", "Desc3", null, null, LocalDateTime.now());
        hecho3.setId(3);

        ArchivoProcesado archivo1 = new ArchivoProcesado("archivo1.csv", LocalDateTime.now(),
                Collections.singletonList(hechoTest));
        ArchivoProcesado archivo2 = new ArchivoProcesado("archivo2.csv", LocalDateTime.now(),
                Collections.singletonList(hecho2));
        ArchivoProcesado archivo3 = new ArchivoProcesado("archivo3.csv", LocalDateTime.now(),
                Collections.singletonList(hecho3));

        when(hechosRepository.findAllArchivosProcesados())
                .thenReturn(Arrays.asList(archivo1, archivo2, archivo3));

        // Act
        List<ArchivoProcesadoDTO> resultado = hechosServices.obtenerHechos(nombresArchivos);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());

        verify(hechosRepository, times(1)).importarHechosSin(nombresArchivos);
        verify(hechosRepository, times(1)).findAllArchivosProcesados();
    }

    @Test
    void obtenerHechos_VerificaMapeoDeDTO_DeberiaMapearCorrectamente() {
        // Arrange
        List<String> nombresArchivos = Collections.singletonList("desastres.csv");
        LocalDateTime fechaCarga = LocalDateTime.of(2025, 12, 15, 10, 30);

        hechoTest.setFechaCarga(fechaCarga);
        ArchivoProcesado archivo = new ArchivoProcesado("desastres.csv", fechaCarga,
                Collections.singletonList(hechoTest));

        when(hechosRepository.findAllArchivosProcesados())
                .thenReturn(Collections.singletonList(archivo));

        // Act
        List<ArchivoProcesadoDTO> resultado = hechosServices.obtenerHechos(nombresArchivos);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());

        ArchivoProcesadoDTO dto = resultado.get(0);
        assertEquals("desastres.csv", dto.getNombre());
        assertNotNull(dto.getFechaCarga());
        assertEquals(1, dto.getHechos().size());

        HechoDTO hechoDTO = dto.getHechos().get(0);
        assertEquals(1, hechoDTO.getId());
        assertEquals("Terremoto", hechoDTO.getTitulo());
        assertEquals("Terremoto en Argentina", hechoDTO.getDescripcion());
    }
}

