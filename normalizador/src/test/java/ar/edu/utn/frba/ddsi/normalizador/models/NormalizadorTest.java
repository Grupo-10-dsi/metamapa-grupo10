package ar.edu.utn.frba.ddsi.normalizador.models;

import ar.edu.utn.frba.ddsi.normalizador.models.dtos.HechoDTO;
import ar.edu.utn.frba.ddsi.normalizador.models.entities.normalizador.INormalizador;
import ar.edu.utn.frba.ddsi.normalizador.models.entities.normalizador.Normalizador;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NormalizadorTest {

    @Test
    void normalizar_CadenaDeNormalizadores_DeberiaAplicarTodosEnOrden() {
        // Arrange
        INormalizador n1 = mock(INormalizador.class);
        INormalizador n2 = mock(INormalizador.class);
        List<INormalizador> normalizadores = Arrays.asList(n1, n2);

        HechoDTO entrada = new HechoDTO();
        entrada.setTitulo("crudo");

        HechoDTO intermedio = new HechoDTO();
        intermedio.setTitulo("parcial");

        HechoDTO salida = new HechoDTO();
        salida.setTitulo("normalizado");

        when(n1.normalizar(entrada)).thenReturn(intermedio);
        when(n2.normalizar(intermedio)).thenReturn(salida);

        Normalizador normalizador = new Normalizador(normalizadores);

        // Act
        HechoDTO resultado = normalizador.normalizar(entrada);

        // Assert
        assertEquals("normalizado", resultado.getTitulo());
        verify(n1, times(1)).normalizar(entrada);
        verify(n2, times(1)).normalizar(intermedio);
    }
}
