package ar.edu.utn.frba.ddsi.estatica.models.entities.dtos;

import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Categoria;
import ar.edu.utn.frba.ddsi.estatica.models.entities.hecho.Ubicacion;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HechoDTOTest {

    @Test
    void constructor_ConTodosLosParametros_DeberiaCrearDTOCorrectamente() {
        // Arrange
        Integer id = 1;
        String titulo = "Terremoto";
        String descripcion = "Terremoto en Argentina";
        Categoria categoria = new Categoria("Desastre Natural");
        Ubicacion ubicacion = new Ubicacion(-34.603722, -58.381592);
        LocalDateTime fechaAcontecimiento = LocalDateTime.now();
        LocalDateTime fechaCarga = LocalDateTime.now();
        List<String> contenidoMultimedia = Arrays.asList("imagen1.jpg", "imagen2.jpg");
        String cuerpo = "Cuerpo del hecho";

        // Act
        HechoDTO dto = new HechoDTO(id, titulo, descripcion, categoria, ubicacion,
                fechaAcontecimiento, fechaCarga, contenidoMultimedia, cuerpo);

        // Assert
        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals(titulo, dto.getTitulo());
        assertEquals(descripcion, dto.getDescripcion());
        assertEquals(categoria, dto.getCategoria());
        assertEquals(ubicacion, dto.getUbicacion());
        assertEquals(fechaAcontecimiento, dto.getFechaAcontecimiento());
        assertEquals(fechaCarga, dto.getFechaCarga());
        assertEquals(contenidoMultimedia, dto.getContenidoMultimedia());
        assertEquals(cuerpo, dto.getCuerpo());
    }

    @Test
    void constructor_SinArgumentos_DeberiaCrearDTOVacio() {
        // Act
        HechoDTO dto = new HechoDTO();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getTitulo());
        assertNull(dto.getDescripcion());
    }

    @Test
    void gettersAndSetters_DeberianFuncionarCorrectamente() {
        // Arrange
        HechoDTO dto = new HechoDTO();

        // Act
        dto.setId(1);
        dto.setTitulo("Nuevo Titulo");
        dto.setDescripcion("Nueva Descripcion");
        dto.setCuerpo("Nuevo Cuerpo");

        // Assert
        assertEquals(1, dto.getId());
        assertEquals("Nuevo Titulo", dto.getTitulo());
        assertEquals("Nueva Descripcion", dto.getDescripcion());
        assertEquals("Nuevo Cuerpo", dto.getCuerpo());
    }

    @Test
    void setContenidoMultimedia_DeberiaActualizarCorrectamente() {
        // Arrange
        HechoDTO dto = new HechoDTO();
        List<String> multimedia = Arrays.asList("video1.mp4", "video2.mp4");

        // Act
        dto.setContenidoMultimedia(multimedia);

        // Assert
        assertEquals(multimedia, dto.getContenidoMultimedia());
        assertEquals(2, dto.getContenidoMultimedia().size());
    }

    @Test
    void constructor_ConMultimediaVacia_DeberiaFuncionarCorrectamente() {
        // Arrange
        List<String> multimedia = Collections.emptyList();

        // Act
        HechoDTO dto = new HechoDTO(1, "Titulo", "Descripcion", null, null,
                LocalDateTime.now(), LocalDateTime.now(), multimedia, "Cuerpo");

        // Assert
        assertNotNull(dto.getContenidoMultimedia());
        assertTrue(dto.getContenidoMultimedia().isEmpty());
    }

    @Test
    void constructor_ConMultimediaMultiple_DeberiaGuardarTodos() {
        // Arrange
        List<String> multimedia = Arrays.asList("imagen1.jpg", "imagen2.jpg", "video1.mp4");

        // Act
        HechoDTO dto = new HechoDTO(1, "Titulo", "Descripcion", null, null,
                LocalDateTime.now(), LocalDateTime.now(), multimedia, "Cuerpo");

        // Assert
        assertEquals(3, dto.getContenidoMultimedia().size());
        assertTrue(dto.getContenidoMultimedia().contains("imagen1.jpg"));
        assertTrue(dto.getContenidoMultimedia().contains("video1.mp4"));
    }

    @Test
    void setFechas_DeberiaActualizarCorrectamente() {
        // Arrange
        HechoDTO dto = new HechoDTO();
        LocalDateTime fecha1 = LocalDateTime.of(2025, 12, 15, 10, 30);
        LocalDateTime fecha2 = LocalDateTime.of(2025, 12, 15, 14, 30);

        // Act
        dto.setFechaAcontecimiento(fecha1);
        dto.setFechaCarga(fecha2);

        // Assert
        assertEquals(fecha1, dto.getFechaAcontecimiento());
        assertEquals(fecha2, dto.getFechaCarga());
    }

    @Test
    void setUbicacion_DeberiaActualizarCorrectamente() {
        // Arrange
        HechoDTO dto = new HechoDTO();
        Ubicacion ubicacion = new Ubicacion(-34.603722, -58.381592);

        // Act
        dto.setUbicacion(ubicacion);

        // Assert
        assertEquals(ubicacion, dto.getUbicacion());
        assertEquals(-34.603722, dto.getUbicacion().getLatitud());
        assertEquals(-58.381592, dto.getUbicacion().getLongitud());
    }

    @Test
    void setCategoria_DeberiaActualizarCorrectamente() {
        // Arrange
        HechoDTO dto = new HechoDTO();
        Categoria categoria = new Categoria("Desastre Natural");

        // Act
        dto.setCategoria(categoria);

        // Assert
        assertEquals(categoria, dto.getCategoria());
    }

    @Test
    void constructor_ConTituloProlongado_DeberiaManejarCorrectamente() {
        // Arrange
        String tituloProlongado = "Este es un titulo muy largo que puede tener muchos caracteres sin problema";

        // Act
        HechoDTO dto = new HechoDTO(1, tituloProlongado, "Descripcion", null, null,
                LocalDateTime.now(), LocalDateTime.now(), null, null);

        // Assert
        assertEquals(tituloProlongado, dto.getTitulo());
    }
}

