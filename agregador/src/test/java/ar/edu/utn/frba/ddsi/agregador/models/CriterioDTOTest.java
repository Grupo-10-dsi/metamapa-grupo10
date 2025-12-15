package ar.edu.utn.frba.ddsi.agregador.models;

import ar.edu.utn.frba.ddsi.agregador.models.entities.dtos.CriterioDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CriterioDTOTest {

    @Test
    void constructor_ConTipoYValor_DeberiaCrearDTOCorrectamente() {
        // Arrange & Act
        CriterioDTO dto = new CriterioDTO("titulo", "Hecho de prueba");

        // Assert
        assertNotNull(dto);
        assertEquals("titulo", dto.getTipo());
        assertEquals("Hecho de prueba", dto.getValor());
    }

    @Test
    void constructor_ConTipoCategoria_DeberiaCrearDTOCorrectamente() {
        // Arrange & Act
        CriterioDTO dto = new CriterioDTO("categoria", "Desastre Natural");

        // Assert
        assertNotNull(dto);
        assertEquals("categoria", dto.getTipo());
        assertEquals("Desastre Natural", dto.getValor());
    }

    @Test
    void constructor_ConTipoDescripcion_DeberiaCrearDTOCorrectamente() {
        // Arrange & Act
        CriterioDTO dto = new CriterioDTO("descripcion", "Descripcion del hecho");

        // Assert
        assertNotNull(dto);
        assertEquals("descripcion", dto.getTipo());
        assertEquals("Descripcion del hecho", dto.getValor());
    }

    @Test
    void constructor_ConTipoFechaDesde_DeberiaCrearDTOCorrectamente() {
        // Arrange & Act
        CriterioDTO dto = new CriterioDTO("fecha_desde", "2024-01-01");

        // Assert
        assertNotNull(dto);
        assertEquals("fecha_desde", dto.getTipo());
        assertEquals("2024-01-01", dto.getValor());
    }

    @Test
    void constructor_ConTipoFechaHasta_DeberiaCrearDTOCorrectamente() {
        // Arrange & Act
        CriterioDTO dto = new CriterioDTO("fecha_hasta", "2024-12-31");

        // Assert
        assertNotNull(dto);
        assertEquals("fecha_hasta", dto.getTipo());
        assertEquals("2024-12-31", dto.getValor());
    }

    @Test
    void constructor_ConTipoUbicacion_DeberiaCrearDTOCorrectamente() {
        // Arrange & Act
        CriterioDTO dto = new CriterioDTO("ubicacion", "-34.603722,-58.381592");

        // Assert
        assertNotNull(dto);
        assertEquals("ubicacion", dto.getTipo());
        assertEquals("-34.603722,-58.381592", dto.getValor());
    }

    @Test
    void getters_DeberianRetornarValoresCorrectos() {
        // Arrange
        String tipo = "titulo";
        String valor = "Valor de prueba";
        CriterioDTO dto = new CriterioDTO(tipo, valor);

        // Act & Assert
        assertEquals(tipo, dto.getTipo());
        assertEquals(valor, dto.getValor());
    }

    @Test
    void constructor_ConValoresVacios_DeberiaCrearDTOCorrectamente() {
        // Arrange & Act
        CriterioDTO dto = new CriterioDTO("", "");

        // Assert
        assertNotNull(dto);
        assertEquals("", dto.getTipo());
        assertEquals("", dto.getValor());
    }

    @Test
    void constructor_ConValorLargo_DeberiaManejarCorrectamente() {
        // Arrange
        String valorLargo = "Este es un valor muy largo para probar que el DTO puede manejar strings extensos sin problemas";

        // Act
        CriterioDTO dto = new CriterioDTO("descripcion", valorLargo);

        // Assert
        assertNotNull(dto);
        assertEquals("descripcion", dto.getTipo());
        assertEquals(valorLargo, dto.getValor());
    }
}

