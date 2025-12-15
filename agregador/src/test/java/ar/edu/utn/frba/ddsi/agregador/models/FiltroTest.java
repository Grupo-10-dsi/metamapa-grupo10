package ar.edu.utn.frba.ddsi.agregador.models;

import ar.edu.utn.frba.ddsi.agregador.models.entities.hecho.Filtro;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FiltroTest {

    @Test
    void constructor_ConTodosLosParametros_DeberiaCrearFiltroCorrectamente() {
        // Arrange & Act
        Filtro filtro = new Filtro(
                "Desastre Natural",
                "2024-01-01",
                "2024-12-31",
                "2024-06-01",
                "2024-06-30",
                -34.603722,
                -58.381592
        );

        // Assert
        assertNotNull(filtro);
        assertEquals("Desastre Natural", filtro.getCategoria());
        assertEquals("2024-01-01", filtro.getFecha_reporte_desde());
        assertEquals("2024-12-31", filtro.getFecha_reporte_hasta());
        assertEquals("2024-06-01", filtro.getFecha_acontecimiento_desde());
        assertEquals("2024-06-30", filtro.getFecha_acontecimiento_hasta());
        assertEquals(-34.603722, filtro.getLatitud());
        assertEquals(-58.381592, filtro.getLongitud());
    }

    @Test
    void constructor_ConParametrosNulos_DeberiaCrearFiltroConNulos() {
        // Arrange & Act
        Filtro filtro = new Filtro(null, null, null, null, null, null, null);

        // Assert
        assertNotNull(filtro);
        assertNull(filtro.getCategoria());
        assertNull(filtro.getFecha_reporte_desde());
        assertNull(filtro.getFecha_reporte_hasta());
        assertNull(filtro.getFecha_acontecimiento_desde());
        assertNull(filtro.getFecha_acontecimiento_hasta());
        assertNull(filtro.getLatitud());
        assertNull(filtro.getLongitud());
    }

    @Test
    void constructor_SoloConCategoria_DeberiaManejarCorrectamente() {
        // Arrange & Act
        Filtro filtro = new Filtro("Accidente", null, null, null, null, null, null);

        // Assert
        assertNotNull(filtro);
        assertEquals("Accidente", filtro.getCategoria());
        assertNull(filtro.getFecha_reporte_desde());
        assertNull(filtro.getFecha_reporte_hasta());
        assertNull(filtro.getFecha_acontecimiento_desde());
        assertNull(filtro.getFecha_acontecimiento_hasta());
        assertNull(filtro.getLatitud());
        assertNull(filtro.getLongitud());
    }

    @Test
    void constructor_SoloConUbicacion_DeberiaManejarCorrectamente() {
        // Arrange & Act
        Filtro filtro = new Filtro(null, null, null, null, null, -34.603722, -58.381592);

        // Assert
        assertNotNull(filtro);
        assertNull(filtro.getCategoria());
        assertEquals(-34.603722, filtro.getLatitud());
        assertEquals(-58.381592, filtro.getLongitud());
    }

    @Test
    void constructor_SoloConFechas_DeberiaManejarCorrectamente() {
        // Arrange & Act
        Filtro filtro = new Filtro(
                null,
                "2024-01-01",
                "2024-12-31",
                "2024-06-01",
                "2024-06-30",
                null,
                null
        );

        // Assert
        assertNotNull(filtro);
        assertNull(filtro.getCategoria());
        assertEquals("2024-01-01", filtro.getFecha_reporte_desde());
        assertEquals("2024-12-31", filtro.getFecha_reporte_hasta());
        assertEquals("2024-06-01", filtro.getFecha_acontecimiento_desde());
        assertEquals("2024-06-30", filtro.getFecha_acontecimiento_hasta());
        assertNull(filtro.getLatitud());
        assertNull(filtro.getLongitud());
    }

    @Test
    void getters_DeberianRetornarValoresCorrectos() {
        // Arrange
        String categoria = "Incendio";
        String fechaReporteDesde = "2024-03-01";
        String fechaReporteHasta = "2024-03-31";
        String fechaAcontecimientoDesde = "2024-03-15";
        String fechaAcontecimientoHasta = "2024-03-20";
        Double latitud = -31.4135;
        Double longitud = -64.1811;

        // Act
        Filtro filtro = new Filtro(
                categoria,
                fechaReporteDesde,
                fechaReporteHasta,
                fechaAcontecimientoDesde,
                fechaAcontecimientoHasta,
                latitud,
                longitud
        );

        // Assert
        assertEquals(categoria, filtro.getCategoria());
        assertEquals(fechaReporteDesde, filtro.getFecha_reporte_desde());
        assertEquals(fechaReporteHasta, filtro.getFecha_reporte_hasta());
        assertEquals(fechaAcontecimientoDesde, filtro.getFecha_acontecimiento_desde());
        assertEquals(fechaAcontecimientoHasta, filtro.getFecha_acontecimiento_hasta());
        assertEquals(latitud, filtro.getLatitud());
        assertEquals(longitud, filtro.getLongitud());
    }

    @Test
    void constructor_ConUbicacionCero_DeberiaManejarCorrectamente() {
        // Arrange & Act
        Filtro filtro = new Filtro(null, null, null, null, null, 0.0, 0.0);

        // Assert
        assertNotNull(filtro);
        assertEquals(0.0, filtro.getLatitud());
        assertEquals(0.0, filtro.getLongitud());
    }

    @Test
    void constructor_ConUbicacionNegativa_DeberiaManejarCorrectamente() {
        // Arrange & Act
        Filtro filtro = new Filtro(null, null, null, null, null, -90.0, -180.0);

        // Assert
        assertNotNull(filtro);
        assertEquals(-90.0, filtro.getLatitud());
        assertEquals(-180.0, filtro.getLongitud());
    }

    @Test
    void constructor_ConUbicacionPositiva_DeberiaManejarCorrectamente() {
        // Arrange & Act
        Filtro filtro = new Filtro(null, null, null, null, null, 45.5, 120.3);

        // Assert
        assertNotNull(filtro);
        assertEquals(45.5, filtro.getLatitud());
        assertEquals(120.3, filtro.getLongitud());
    }
}

