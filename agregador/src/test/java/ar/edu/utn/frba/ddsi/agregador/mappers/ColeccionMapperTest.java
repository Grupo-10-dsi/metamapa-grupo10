package ar.edu.utn.frba.ddsi.agregador.mappers;

import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Algoritmo_Consenso;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Coleccion;
import ar.edu.utn.frba.ddsi.agregador.models.entities.coleccion.Fuente;
import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.ColeccionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ColeccionMapperTest {

    private ColeccionMapper coleccionMapper;

    @BeforeEach
    void setUp() {
        coleccionMapper = Mappers.getMapper(ColeccionMapper.class);
    }

    @Test
    void toColeccionDTO_ConColeccionCompleta_DeberiaMapearnCorrectamente() {
        // Arrange
        Fuente fuente1 = new Fuente("http://fuente1.com", "Fuente 1");
        Fuente fuente2 = new Fuente("http://fuente2.com", "Fuente 2");

        Coleccion coleccion = new Coleccion();
        coleccion.setId(1);
        coleccion.setTitulo("Coleccion Test");
        coleccion.setDescripcion("Descripcion test");
        coleccion.setAlgoritmo_consenso(Algoritmo_Consenso.MULTIPLES_MENCIONES);
        coleccion.setFuentes(Arrays.asList(fuente1, fuente2));
        coleccion.setCriterios(new ArrayList<>());

        // Act
        ColeccionDTO resultado = coleccionMapper.toColeccionDTO(coleccion);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("Coleccion Test", resultado.getTitulo());
        assertEquals("Descripcion test", resultado.getDescripcion());
        assertEquals(Algoritmo_Consenso.MULTIPLES_MENCIONES, resultado.getAlgoritmo_consenso());
        assertNotNull(resultado.getUrls_fuente());
        assertEquals(2, resultado.getUrls_fuente().size());
        assertTrue(resultado.getUrls_fuente().contains("http://fuente1.com"));
        assertTrue(resultado.getUrls_fuente().contains("http://fuente2.com"));
    }

    @Test
    void toColeccionDTO_ConColeccionSinFuentes_DeberiaDevolverListaVacia() {
        // Arrange
        Coleccion coleccion = new Coleccion();
        coleccion.setId(1);
        coleccion.setTitulo("Coleccion Sin Fuentes");
        coleccion.setDescripcion("Sin fuentes");
        coleccion.setAlgoritmo_consenso(Algoritmo_Consenso.NINGUNO);
        coleccion.setFuentes(new ArrayList<>());
        coleccion.setCriterios(new ArrayList<>());

        // Act
        ColeccionDTO resultado = coleccionMapper.toColeccionDTO(coleccion);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getUrls_fuente());
        assertTrue(resultado.getUrls_fuente().isEmpty());
    }

    @Test
    void toColeccionDTO_ConFuentesNull_DeberiaDevolverListaVacia() {
        // Arrange
        Coleccion coleccion = new Coleccion();
        coleccion.setId(1);
        coleccion.setTitulo("Coleccion Null Fuentes");
        coleccion.setDescripcion("Fuentes nulas");
        coleccion.setAlgoritmo_consenso(Algoritmo_Consenso.MAYORIA_SIMPLE);
        coleccion.setFuentes(null);
        coleccion.setCriterios(new ArrayList<>());

        // Act
        ColeccionDTO resultado = coleccionMapper.toColeccionDTO(coleccion);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getUrls_fuente());
        assertTrue(resultado.getUrls_fuente().isEmpty());
    }

    @Test
    void mapFuentesToUrls_ConListaDeFuentes_DeberiaConvertirAUrls() {
        // Arrange
        Fuente fuente1 = new Fuente("http://test1.com", "Test 1");
        Fuente fuente2 = new Fuente("http://test2.com", "Test 2");
        Fuente fuente3 = new Fuente("http://test3.com", "Test 3");
        List<Fuente> fuentes = Arrays.asList(fuente1, fuente2, fuente3);

        // Act
        List<String> resultado = coleccionMapper.mapFuentesToUrls(fuentes);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("http://test1.com", resultado.get(0));
        assertEquals("http://test2.com", resultado.get(1));
        assertEquals("http://test3.com", resultado.get(2));
    }

    @Test
    void mapFuentesToUrls_ConListaVacia_DeberiaRetornarListaVacia() {
        // Arrange
        List<Fuente> fuentes = new ArrayList<>();

        // Act
        List<String> resultado = coleccionMapper.mapFuentesToUrls(fuentes);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void mapFuentesToUrls_ConNull_DeberiaRetornarListaVacia() {
        // Act
        List<String> resultado = coleccionMapper.mapFuentesToUrls(null);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void toColeccionDTO_ConDiferentesAlgoritmosConsenso_DeberiaMapearnCorrectamente() {
        // Test con cada algoritmo de consenso
        Algoritmo_Consenso[] algoritmos = Algoritmo_Consenso.values();

        for (Algoritmo_Consenso algoritmo : algoritmos) {
            // Arrange
            Coleccion coleccion = new Coleccion();
            coleccion.setId(1);
            coleccion.setTitulo("Test " + algoritmo);
            coleccion.setDescripcion("Test");
            coleccion.setAlgoritmo_consenso(algoritmo);
            coleccion.setFuentes(new ArrayList<>());
            coleccion.setCriterios(new ArrayList<>());

            // Act
            ColeccionDTO resultado = coleccionMapper.toColeccionDTO(coleccion);

            // Assert
            assertNotNull(resultado);
            assertEquals(algoritmo, resultado.getAlgoritmo_consenso());
        }
    }

    @Test
    void toColeccionDTO_ConUnaSolaFuente_DeberiaMapearnCorrectamente() {
        // Arrange
        Fuente fuente = new Fuente("http://unica-fuente.com", "Unica Fuente");

        Coleccion coleccion = new Coleccion();
        coleccion.setId(1);
        coleccion.setTitulo("Coleccion Una Fuente");
        coleccion.setDescripcion("Solo una fuente");
        coleccion.setAlgoritmo_consenso(Algoritmo_Consenso.ABSOLUTA);
        coleccion.setFuentes(Arrays.asList(fuente));
        coleccion.setCriterios(new ArrayList<>());

        // Act
        ColeccionDTO resultado = coleccionMapper.toColeccionDTO(coleccion);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getUrls_fuente().size());
        assertEquals("http://unica-fuente.com", resultado.getUrls_fuente().get(0));
    }
}

