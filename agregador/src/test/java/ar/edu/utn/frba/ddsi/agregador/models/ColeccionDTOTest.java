package ar.edu.utn.frba.ddsi.agregador.models.entities.dtos;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Algoritmo_Consenso;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColeccionDTOTest {

    @Test
    void constructor_ConTodosLosParametros_DeberiaCrearDTOCorrectamente() {
        // Arrange
        Integer id = 1;
        String titulo = "Coleccion Test";
        String descripcion = "Descripcion test";
        Algoritmo_Consenso algoritmo = Algoritmo_Consenso.MULTIPLES_MENCIONES;
        List<String> urls = Arrays.asList("http://fuente1.com", "http://fuente2.com");
        List<CriterioDTO> criterios = Arrays.asList(
                new CriterioDTO("titulo", "Test"),
                new CriterioDTO("categoria", "Desastre")
        );

        // Act
        ColeccionDTO dto = new ColeccionDTO(id, titulo, descripcion, algoritmo, urls, criterios);

        // Assert
        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(titulo, dto.getTitulo());
        assertEquals(descripcion, dto.getDescripcion());
        assertEquals(algoritmo, dto.getAlgoritmo_consenso());
        assertEquals(2, dto.getUrls_fuente().size());
        assertEquals(2, dto.getCriterios().size());
    }

    @Test
    void constructor_ConIdNull_DeberiaCrearDTOCorrectamente() {
        // Arrange & Act
        ColeccionDTO dto = new ColeccionDTO(
                null,
                "Nueva Coleccion",
                "Sin ID",
                Algoritmo_Consenso.MAYORIA_SIMPLE,
                Arrays.asList("http://test.com"),
                Arrays.asList()
        );

        // Assert
        assertNotNull(dto);
        assertNull(dto.getId());
        assertEquals("Nueva Coleccion", dto.getTitulo());
    }

    @Test
    void getters_DeberianRetornarValoresCorrectos() {
        // Arrange
        ColeccionDTO dto = new ColeccionDTO(
                1,
                "Test",
                "Descripcion",
                Algoritmo_Consenso.ABSOLUTA,
                Arrays.asList("http://url.com"),
                Arrays.asList()
        );

        // Act & Assert
        assertEquals(1, dto.getId());
        assertEquals("Test", dto.getTitulo());
        assertEquals("Descripcion", dto.getDescripcion());
        assertEquals(Algoritmo_Consenso.ABSOLUTA, dto.getAlgoritmo_consenso());
        assertNotNull(dto.getUrls_fuente());
        assertNotNull(dto.getCriterios());
    }

    @Test
    void constructor_ConVariasUrls_DeberiaMantenerOrden() {
        // Arrange
        List<String> urls = Arrays.asList("url1", "url2", "url3", "url4");

        // Act
        ColeccionDTO dto = new ColeccionDTO(
                1,
                "Test",
                "Test",
                Algoritmo_Consenso.NINGUNO,
                urls,
                Arrays.asList()
        );

        // Assert
        assertEquals(4, dto.getUrls_fuente().size());
        assertEquals("url1", dto.getUrls_fuente().get(0));
        assertEquals("url4", dto.getUrls_fuente().get(3));
    }
}

