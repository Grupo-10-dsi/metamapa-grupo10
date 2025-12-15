package ar.edu.utn.frba.ddsi.estatica.models;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Hecho;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Origen_Fuente;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Ubicacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HechoTest {

    @Test
    void constructor_ConTodosLosParametros_DeberiaCrearHechoCorrectamente() {
        // Arrange
        String titulo = "Terremoto";
        String descripcion = "Terremoto en Argentina";
        Categoria categoria = null;
        Ubicacion ubicacion = null;
        LocalDateTime fechaAcontecimiento = LocalDateTime.now();

        // Act
        Hecho hecho = new Hecho(titulo, descripcion, categoria, ubicacion, fechaAcontecimiento);

        // Assert
        assertNotNull(hecho);
        assertEquals(titulo, hecho.getTitulo());
        assertEquals(descripcion, hecho.getDescripcion());
        assertEquals(categoria, hecho.getCategoria());
        assertEquals(ubicacion, hecho.getUbicacion());
        assertEquals(fechaAcontecimiento, hecho.getFechaAcontecimiento());
        assertNotNull(hecho.getFechaCarga());
        assertEquals(Origen_Fuente.ESTATICA, hecho.getOrigenFuente());
    }

    @Test
    void constructor_DeberiaAsignarFechaCargaEnElMomento() {
        // Arrange
        LocalDateTime antes = LocalDateTime.now();

        // Act
        Hecho hecho = new Hecho("Titulo", "Descripcion", null, null, LocalDateTime.now());

        LocalDateTime despues = LocalDateTime.now();

        // Assert
        assertNotNull(hecho.getFechaCarga());
        assertTrue(hecho.getFechaCarga().isAfter(antes.minusSeconds(1)));
        assertTrue(hecho.getFechaCarga().isBefore(despues.plusSeconds(1)));
    }

    @Test
    void constructor_DeberiaAsignarOrigenFuenteComoESTATICA() {
        // Act
        Hecho hecho = new Hecho("Titulo", "Descripcion", null, null, LocalDateTime.now());

        // Assert
        assertEquals(Origen_Fuente.ESTATICA, hecho.getOrigenFuente());
    }

    @Test
    void gettersAndSetters_DeberiaFuncionarCorrectamente() {
        // Arrange
        Hecho hecho = new Hecho("Titulo", "Descripcion", null, null, LocalDateTime.now());
        Integer nuevoId = 5;
        String nuevoTitulo = "Nuevo Titulo";
        String nuevaDescripcion = "Nueva Descripcion";

        // Act
        hecho.setId(nuevoId);
        hecho.setTitulo(nuevoTitulo);
        hecho.setDescripcion(nuevaDescripcion);

        // Assert
        assertEquals(nuevoId, hecho.getId());
        assertEquals(nuevoTitulo, hecho.getTitulo());
        assertEquals(nuevaDescripcion, hecho.getDescripcion());
    }

    @Test
    void setFechaCarga_DeberiaActualizarCorrectamente() {
        // Arrange
        Hecho hecho = new Hecho("Titulo", "Descripcion", null, null, LocalDateTime.now());
        LocalDateTime nuevaFecha = LocalDateTime.of(2025, 12, 15, 10, 30);

        // Act
        hecho.setFechaCarga(nuevaFecha);

        // Assert
        assertEquals(nuevaFecha, hecho.getFechaCarga());
    }

    @Test
    void constructor_ConDatosCompletos_DeberiaGuardarTodos() {
        // Arrange
        String titulo = "Inundacion";
        String descripcion = "Inundacion en Buenos Aires";
        LocalDateTime fecha = LocalDateTime.of(2025, 12, 15, 14, 30);

        // Act
        Hecho hecho = new Hecho(titulo, descripcion, null, null, fecha);
        hecho.setId(1);

        // Assert
        assertEquals(1, hecho.getId());
        assertEquals("Inundacion", hecho.getTitulo());
        assertEquals("Inundacion en Buenos Aires", hecho.getDescripcion());
        assertEquals(fecha, hecho.getFechaAcontecimiento());
        assertNotNull(hecho.getFechaCarga());
    }

    @Test
    void setOrigenFuente_DeberiaActualizarCorrectamente() {
        // Arrange
        Hecho hecho = new Hecho("Titulo", "Descripcion", null, null, LocalDateTime.now());

        // Act
        hecho.setOrigenFuente(Origen_Fuente.DINAMICA);

        // Assert
        assertEquals(Origen_Fuente.DINAMICA, hecho.getOrigenFuente());
    }

    @Test
    void constructor_ConFechasNulas_DeberiaFuncionarCorrectamente() {
        // Act
        Hecho hecho = new Hecho("Titulo", "Descripcion", null, null, null);

        // Assert
        assertNotNull(hecho);
        assertNull(hecho.getFechaAcontecimiento());
        assertNotNull(hecho.getFechaCarga());
    }

    @Test
    void setUbicacion_DeberiaActualizarCorrectamente() {
        // Arrange
        Hecho hecho = new Hecho("Titulo", "Descripcion", null, null, LocalDateTime.now());
        Ubicacion nuevaUbicacion = new Ubicacion(-34.603722, -58.381592);

        // Act
        hecho.setUbicacion(nuevaUbicacion);

        // Assert
        assertEquals(nuevaUbicacion, hecho.getUbicacion());
    }

    @Test
    void setCategoria_DeberiaActualizarCorrectamente() {
        // Arrange
        Hecho hecho = new Hecho("Titulo", "Descripcion", null, null, LocalDateTime.now());
        Categoria nuevaCategoria = new Categoria( "Desastre Natural");

        // Act
        hecho.setCategoria(nuevaCategoria);

        // Assert
        assertEquals(nuevaCategoria, hecho.getCategoria());
    }
}

