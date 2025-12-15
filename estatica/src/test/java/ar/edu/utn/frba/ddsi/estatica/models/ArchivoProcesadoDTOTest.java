package ar.edu.utn.frba.ddsi.estatica.models;

import ar.edu.utn.frba.ddsi.estatica.models.entities.dtos.ArchivoProcesadoDTO;
import ar.edu.utn.frba.ddsi.estatica.models.entities.dtos.HechoDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArchivoProcesadoDTOTest {

    @Test
    void constructor_ConTodosLosParametros_DeberiaCrearDTOCorrectamente() {
        // Arrange
        String nombre = "desastres.csv";
        LocalDateTime fechaCarga = LocalDateTime.now();
        HechoDTO hecho1 = new HechoDTO(1, "Terremoto", "Descripcion", null, null,
                LocalDateTime.now(), LocalDateTime.now(), null, null);
        HechoDTO hecho2 = new HechoDTO(2, "Inundacion", "Descripcion", null, null,
                LocalDateTime.now(), LocalDateTime.now(), null, null);
        List<HechoDTO> hechos = Arrays.asList(hecho1, hecho2);

        // Act
        ArchivoProcesadoDTO dto = new ArchivoProcesadoDTO(nombre, fechaCarga, hechos);

        // Assert
        assertNotNull(dto);
        assertEquals(nombre, dto.getNombre());
        assertEquals(fechaCarga, dto.getFechaCarga());
        assertEquals(hechos, dto.getHechos());
        assertEquals(2, dto.getHechos().size());
    }

    @Test
    void constructor_ConHechosVacio_DeberiaFuncionarCorrectamente() {
        // Arrange
        String nombre = "archivo_vacio.csv";
        LocalDateTime fechaCarga = LocalDateTime.now();
        List<HechoDTO> hechos = Collections.emptyList();

        // Act
        ArchivoProcesadoDTO dto = new ArchivoProcesadoDTO(nombre, fechaCarga, hechos);

        // Assert
        assertNotNull(dto);
        assertEquals(nombre, dto.getNombre());
        assertNotNull(dto.getHechos());
        assertTrue(dto.getHechos().isEmpty());
    }

    @Test
    void constructor_ConUnSoloHecho_DeberiaFuncionarCorrectamente() {
        // Arrange
        String nombre = "desastres.csv";
        LocalDateTime fechaCarga = LocalDateTime.now();
        HechoDTO hecho = new HechoDTO(1, "Desastre", "Descripcion", null, null,
                LocalDateTime.now(), LocalDateTime.now(), null, null);
        List<HechoDTO> hechos = Collections.singletonList(hecho);

        // Act
        ArchivoProcesadoDTO dto = new ArchivoProcesadoDTO(nombre, fechaCarga, hechos);

        // Assert
        assertNotNull(dto);
        assertEquals(1, dto.getHechos().size());
        assertEquals("Desastre", dto.getHechos().get(0).getTitulo());
    }

    @Test
    void getNombre_DeberiaRetornarNombreCorrectamente() {
        // Arrange
        String nombre = "terremotos.csv";
        ArchivoProcesadoDTO dto = new ArchivoProcesadoDTO(nombre, LocalDateTime.now(),
                Collections.emptyList());

        // Act
        String resultado = dto.getNombre();

        // Assert
        assertEquals(nombre, resultado);
    }

    @Test
    void getFechaCarga_DeberiaRetornarFechaCorrectamente() {
        // Arrange
        LocalDateTime fechaCarga = LocalDateTime.of(2025, 12, 15, 10, 30);
        ArchivoProcesadoDTO dto = new ArchivoProcesadoDTO("archivo.csv", fechaCarga,
                Collections.emptyList());

        // Act
        LocalDateTime resultado = dto.getFechaCarga();

        // Assert
        assertEquals(fechaCarga, resultado);
    }

    @Test
    void getHechos_DeberiaRetornarListaDeTodosLosHechos() {
        // Arrange
        List<HechoDTO> hechos = Arrays.asList(
                new HechoDTO(1, "Hecho1", "Desc1", null, null,
                        LocalDateTime.now(), LocalDateTime.now(), null, null),
                new HechoDTO(2, "Hecho2", "Desc2", null, null,
                        LocalDateTime.now(), LocalDateTime.now(), null, null),
                new HechoDTO(3, "Hecho3", "Desc3", null, null,
                        LocalDateTime.now(), LocalDateTime.now(), null, null)
        );
        ArchivoProcesadoDTO dto = new ArchivoProcesadoDTO("archivo.csv", LocalDateTime.now(), hechos);

        // Act
        List<HechoDTO> resultado = dto.getHechos();

        // Assert
        assertEquals(3, resultado.size());
        assertEquals("Hecho1", resultado.get(0).getTitulo());
        assertEquals("Hecho3", resultado.get(2).getTitulo());
    }

    @Test
    void constructor_ConNombreProlongado_DeberiaManejarCorrectamente() {
        // Arrange
        String nombre = "archivo_con_nombre_muy_prolongado_y_descriptivo_2025_12_15.csv";
        ArchivoProcesadoDTO dto = new ArchivoProcesadoDTO(nombre, LocalDateTime.now(),
                Collections.emptyList());

        // Assert
        assertEquals(nombre, dto.getNombre());
    }

    @Test
    void constructor_ConHechosMultiples_DeberiaMantenerlaTotalidad() {
        // Arrange
        List<HechoDTO> hechos = Arrays.asList(
                new HechoDTO(1, "H1", "D1", null, null, LocalDateTime.now(), LocalDateTime.now(), null, null),
                new HechoDTO(2, "H2", "D2", null, null, LocalDateTime.now(), LocalDateTime.now(), null, null),
                new HechoDTO(3, "H3", "D3", null, null, LocalDateTime.now(), LocalDateTime.now(), null, null),
                new HechoDTO(4, "H4", "D4", null, null, LocalDateTime.now(), LocalDateTime.now(), null, null),
                new HechoDTO(5, "H5", "D5", null, null, LocalDateTime.now(), LocalDateTime.now(), null, null)
        );

        // Act
        ArchivoProcesadoDTO dto = new ArchivoProcesadoDTO("archivo.csv", LocalDateTime.now(), hechos);

        // Assert
        assertEquals(5, dto.getHechos().size());
        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, dto.getHechos().get(i).getId());
        }
    }
}

